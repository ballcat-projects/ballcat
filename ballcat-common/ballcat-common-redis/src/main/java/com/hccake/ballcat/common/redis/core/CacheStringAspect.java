package com.hccake.ballcat.common.redis.core;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import com.hccake.ballcat.common.redis.core.annotation.CacheDel;
import com.hccake.ballcat.common.redis.core.annotation.CachePut;
import com.hccake.ballcat.common.redis.core.annotation.Cached;
import com.hccake.ballcat.common.redis.operation.CacheDelOps;
import com.hccake.ballcat.common.redis.operation.CachedOps;
import com.hccake.ballcat.common.redis.operation.CachePutOps;
import com.hccake.ballcat.common.redis.operation.function.VoidMethod;
import com.hccake.ballcat.common.redis.serialize.CacheSerializer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 为保证缓存更新无异常，该切面优先级必须高于事务切面
 *
 * @author Hccake
 * @date 2019/8/31 18:01
 * @version 1.0
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

	@Pointcut("execution(@(@com.hccake.ballcat.common.redis.core.annotation.MetaCacheAnnotation *) * *(..))")
	public void pointCut() {
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {

		// 获取目标方法
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();

		log.trace("=======The string cache aop is executed! method : {}", method.getName());

		// 根据方法的参数 以及当前类对象获得 keyGenerator
		Object target = point.getTarget();
		Object[] arguments = point.getArgs();
		KeyGenerator keyGenerator = new KeyGenerator(target, method, arguments);

		ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

		// 获取注解对象
		Cached cachedAnnotation = AnnotationUtils.getAnnotation(method, Cached.class);
		if (cachedAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachedAnnotation.key(), cachedAnnotation.keyJoint());
			// redis 分布式锁的 key
			String lockKey = key + CachePropertiesHolder.lockKeySuffix();
			Supplier<String> cacheQuery = () -> valueOperations.get(key);
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachedAnnotation.ttl());
			return cached(new CachedOps(point, lockKey, cacheQuery, cachePut, method.getGenericReturnType()));

		}

		CachePut cachePutAnnotation = AnnotationUtils.getAnnotation(method, CachePut.class);
		if (cachePutAnnotation != null) {
			// 缓存key
			String key = keyGenerator.getKey(cachePutAnnotation.key(), cachePutAnnotation.keyJoint());
			// 失效时间控制
			Consumer<Object> cachePut = prodCachePutFunction(valueOperations, key, cachePutAnnotation.ttl());
			return cachePut(new CachePutOps(point, cachePut));
		}

		CacheDel cacheDelAnnotation = AnnotationUtils.getAnnotation(method, CacheDel.class);
		if (cacheDelAnnotation != null) {
			VoidMethod cacheDel;
			if (cacheDelAnnotation.multiDel()) {
				Collection<String> keys = keyGenerator.getKeys(cacheDelAnnotation.key(), cacheDelAnnotation.keyJoint());
				cacheDel = () -> redisTemplate.delete(keys);
			}
			else {
				// 缓存key
				String key = keyGenerator.getKey(cacheDelAnnotation.key(), cacheDelAnnotation.keyJoint());
				cacheDel = () -> redisTemplate.delete(key);
			}
			return cacheDel(new CacheDelOps(point, cacheDel));
		}

		return point.proceed();
	}

	private Consumer<Object> prodCachePutFunction(ValueOperations<String, String> valueOperations, String key,
			long ttl) {
		Consumer<Object> cachePut;
		if (ttl < 0) {
			cachePut = value -> valueOperations.set(key, (String) value);
		}
		else if (ttl == 0) {
			cachePut = value -> valueOperations.set(key, (String) value, CachePropertiesHolder.expireTime(),
					TimeUnit.SECONDS);
		}
		else {
			cachePut = value -> valueOperations.set(key, (String) value, ttl, TimeUnit.SECONDS);
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
			return cacheSerializer.deserialize(cacheData, dataClazz);
		}

		// 2.==========如果缓存为空 则需查询数据库并更新===============
		Object dbData = null;
		// 尝试获取锁，只允许一个线程更新缓存
		String reqId = UUID.randomUUID().toString();
		if (CacheLock.lock(ops.lockKey(), reqId)) {
			// 有可能其他线程已经更新缓存，这里再次判断缓存是否为空
			cacheData = cacheQuery.get();
			if (cacheData == null) {
				// 从数据库查询数据
				dbData = ops.joinPoint().proceed();
				// 如果数据库中没数据，填充一个String，防止缓存击穿
				cacheData = dbData == null ? CachePropertiesHolder.nullValue() : cacheSerializer.serialize(dbData);
				// 设置缓存
				ops.cachePut().accept(cacheData);
			}
			// 解锁
			CacheLock.releaseLock(ops.lockKey(), reqId);
			// 返回数据
			return dbData;
		}
		else {
			cacheData = cacheQuery.get();
		}

		// 自旋时间内未获取到锁，或者数据库中数据为空，返回null
		if (cacheData == null || ops.nullValue(cacheData)) {
			return null;
		}
		return cacheSerializer.deserialize(cacheData, dataClazz);
	}

	/**
	 * 缓存操作模板方法
	 */
	public Object cachePut(CachePutOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();

		// 将返回值放置入缓存中
		String cacheData = data == null ? CachePropertiesHolder.nullValue() : cacheSerializer.serialize(data);
		ops.cachePut().accept(cacheData);

		return data;
	}

	/**
	 * 缓存删除的模板方法 在目标方法执行后 执行删除
	 */
	public Object cacheDel(CacheDelOps ops) throws Throwable {

		// 先执行目标方法 并拿到返回值
		Object data = ops.joinPoint().proceed();
		// 将删除缓存
		ops.cacheDel().run();

		return data;
	}

}
