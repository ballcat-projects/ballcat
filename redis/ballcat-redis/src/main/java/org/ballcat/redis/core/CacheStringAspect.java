/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.redis.core;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.ballcat.redis.RedisHelper;
import org.ballcat.redis.config.CachePropertiesHolder;
import org.ballcat.redis.core.annotation.CacheDel;
import org.ballcat.redis.core.annotation.CacheDels;
import org.ballcat.redis.core.annotation.CachePut;
import org.ballcat.redis.core.annotation.Cached;
import org.ballcat.redis.lock.DistributedLock;
import org.ballcat.redis.operation.CacheDelOps;
import org.ballcat.redis.operation.CacheDelsOps;
import org.ballcat.redis.operation.CachePutOps;
import org.ballcat.redis.operation.CachedOps;
import org.ballcat.redis.operation.function.VoidMethod;
import org.ballcat.redis.serialize.CacheSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

/**
 * 为保证缓存更新无异常，该切面优先级必须高于事务切面
 *
 * @author Hccake 2019/8/31 18:01
 */
@Aspect
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class CacheStringAspect {

	Logger log = LoggerFactory.getLogger(CacheStringAspect.class);

	private final CacheSerializer cacheSerializer;

	private final StringRedisTemplate redisTemplate;

	public CacheStringAspect(StringRedisTemplate redisTemplate, CacheSerializer cacheSerializer) {
		this.redisTemplate = redisTemplate;
		this.cacheSerializer = cacheSerializer;
	}

	@Pointcut("execution(@(@org.ballcat.redis.core.annotation.MetaCacheAnnotation *) * *(..))")
	public void pointCut() {
		// do nothing
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		// 获取目标方法
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		if (this.log.isTraceEnabled()) {
			this.log.trace("=======The string cache aop is executed! method : {}", method.getName());
		}

		// 根据方法的参数 以及当前类对象获得 keyGenerator
		Object target = point.getTarget();
		Object[] arguments = point.getArgs();
		KeyGenerator keyGenerator = new KeyGenerator(target, method, arguments);

		ValueOperations<String, String> valueOperations = this.redisTemplate.opsForValue();

		// 缓存处理
		Cached cachedAnnotation = AnnotationUtils.getAnnotation(method, Cached.class);
		if (cachedAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachedAnnotation.key(), cachedAnnotation.keyJoint());
			// redis 分布式锁的 key
			String lockKey = key + CachePropertiesHolder.lockKeySuffix();
			Supplier<String> cacheQuery = () -> valueOperations.get(key);
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachedAnnotation.ttl(),
					cachedAnnotation.timeUnit());
			CachedOps ops = new CachedOps(point, lockKey, cacheQuery, cachePut, method.getGenericReturnType(),
					cachedAnnotation.retryCount());
			return cached(ops);
		}

		// 缓存更新处理
		CachePut cachePutAnnotation = AnnotationUtils.getAnnotation(method, CachePut.class);
		if (cachePutAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachePutAnnotation.key(), cachePutAnnotation.keyJoint());
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachePutAnnotation.ttl(),
					cachePutAnnotation.timeUnit());
			return cachePut(new CachePutOps(point, cachePut));
		}

		// 缓存删除处理
		CacheDel cacheDelAnnotation = AnnotationUtils.getAnnotation(method, CacheDel.class);
		if (cacheDelAnnotation != null) {
			return cacheDel(new CacheDelOps(point, buildCacheDelExecution(cacheDelAnnotation, keyGenerator)));
		}

		// 多个缓存删除处理
		CacheDels cacheDelsAnnotation = AnnotationUtils.getAnnotation(method, CacheDels.class);
		if (cacheDelsAnnotation != null) {
			int annotationCount = cacheDelsAnnotation.value().length;
			VoidMethod[] cacheDels = new VoidMethod[annotationCount];
			for (int i = 0; i < annotationCount; i++) {
				cacheDels[i] = buildCacheDelExecution(cacheDelsAnnotation.value()[i], keyGenerator);
			}
			return cacheDels(new CacheDelsOps(point, cacheDels));
		}

		return point.proceed();
	}

	private Consumer<Object> prodCachePutFunction(ValueOperations<String, String> valueOperations, String key, long ttl,
			TimeUnit unit) {
		Consumer<Object> cachePut;
		if (ttl < 0) {
			cachePut = value -> valueOperations.set(key, (String) value);
		}
		else if (ttl == 0) {
			cachePut = value -> valueOperations.set(key, (String) value, CachePropertiesHolder.expireTime(), unit);
		}
		else {
			cachePut = value -> valueOperations.set(key, (String) value, ttl, unit);
		}
		return cachePut;
	}

	/**
	 * cached 类型的模板方法 1. 先查缓存 若有数据则直接返回 2. 尝试获取锁 若成功执行目标方法（一般是去查数据库） 3. 将数据库获取到数据同步至缓存
	 * @param ops 缓存操作类
	 * @return result
	 * @throws IOException IO 异常
	 */
	public Object cached(CachedOps ops) throws Throwable {

		// 缓存查询方法
		Supplier<String> cacheQuery = ops.cacheQuery();
		// 返回数据类型
		Type dataClazz = ops.returnType();

		// 1.==================尝试从缓存获取数据==========================
		String cacheData = cacheQuery.get();
		// 如果是空值 则return null | 不是空值且不是null 则直接返回
		if (ops.nullValue(cacheData)) {
			return null;
		}
		else if (cacheData != null) {
			return this.cacheSerializer.deserialize(cacheData, dataClazz);
		}

		// 2.==========如果缓存为空 则需查询数据库并更新===============
		cacheData = DistributedLock.<String>instance().action(ops.lockKey(), () -> {
			String cacheValue = cacheQuery.get();
			if (cacheValue == null) {
				// 从数据库查询数据
				Object dbValue = ops.joinPoint().proceed();
				// 如果数据库中没数据，填充一个String，防止缓存击穿
				cacheValue = dbValue == null ? CachePropertiesHolder.nullValue()
						: this.cacheSerializer.serialize(dbValue);
				// 设置缓存
				ops.cachePut().accept(cacheValue);
			}
			return cacheValue;
		}).onLockFail(cacheQuery).retryCount(ops.retryCount()).lock();
		// 自旋时间内未获取到锁，或者数据库中数据为空，返回null
		if (cacheData == null || ops.nullValue(cacheData)) {
			return null;
		}
		return this.cacheSerializer.deserialize(cacheData, dataClazz);
	}

	/**
	 * 缓存操作模板方法
	 */
	private Object cachePut(CachePutOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();

		// 将返回值放置入缓存中
		String cacheData = data == null ? CachePropertiesHolder.nullValue() : this.cacheSerializer.serialize(data);
		ops.cachePut().accept(cacheData);

		return data;
	}

	/**
	 * 缓存删除的模板方法 在目标方法执行后 执行删除
	 */
	private Object cacheDel(CacheDelOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将删除缓存
		ops.cacheDel().run();

		return data;
	}

	/**
	 * 缓存批量删除的模板方法 在目标方法执行后 执行删除
	 */
	private Object cacheDels(CacheDelsOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将删除缓存
		for (VoidMethod voidMethod : ops.cacheDel()) {
			voidMethod.run();
		}
		return data;
	}

	/**
	 * 构建缓存删除执行方法
	 * @param cacheDelAnnotation 缓存删除注解
	 * @param keyGenerator 缓存键生成器
	 * @return 用于执行的无返回值方法
	 */
	private VoidMethod buildCacheDelExecution(CacheDel cacheDelAnnotation, KeyGenerator keyGenerator) {
		VoidMethod cacheDel;
		if (cacheDelAnnotation.allEntries()) {
			// 优先判断是否是删除名称空间下所有的键值对
			cacheDel = () -> {
				Cursor<String> scan = RedisHelper.scan(cacheDelAnnotation.key().concat("*"));
				while (scan.hasNext()) {
					this.redisTemplate.delete(scan.next());
				}
				if (!scan.isClosed()) {
					scan.close();
				}
			};
		}
		else {
			if (cacheDelAnnotation.multiDel()) {
				Collection<String> keys = keyGenerator.getKeys(cacheDelAnnotation.key(), cacheDelAnnotation.keyJoint());
				cacheDel = () -> this.redisTemplate.delete(keys);
			}
			else {
				// 缓存key
				String key = keyGenerator.getKey(cacheDelAnnotation.key(), cacheDelAnnotation.keyJoint());
				cacheDel = () -> this.redisTemplate.delete(key);
			}
		}
		return cacheDel;
	}

}
