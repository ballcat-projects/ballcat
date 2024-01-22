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

package org.ballcat.redis;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.value.WaitValue;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStreamCommands;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.Record;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.connection.stream.StreamRecords;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.lang.Nullable;

/**
 * Redis 操作的辅助类
 *
 * @author lingting
 * @author svn
 * @author hccake
 */
@SuppressWarnings("ConstantConditions")
@Slf4j
public final class RedisHelper {

	@SuppressWarnings("InstantiationOfUtilityClass")
	public static final RedisHelper INSTANCE = new RedisHelper();

	private RedisHelper() {
	}

	/**
	 * 自增并设置过期时间的 lua 脚本
	 */
	private static final DefaultRedisScript<Long> INCR_BY_EXPIRE_LUA_SCRIPT = new DefaultRedisScript<>(
			"local r = redis.call('INCRBY', KEYS[1], ARGV[1]) redis.call('EXPIRE', KEYS[1], ARGV[2]) return r",
			Long.class);

	static WaitValue<RedisTemplate<String, String>> redisTemplateValue = WaitValue.of();

	@SneakyThrows
	public static RedisTemplate<String, String> getRedisTemplate() {
		return redisTemplateValue.notNull();
	}

	@SneakyThrows
	public static void setRedisTemplate(RedisTemplate<String, String> redisTemplate) {
		redisTemplateValue.update(redisTemplate);
	}

	@SuppressWarnings("all")
	private static RedisSerializer<String> getKeySerializer() {
		return (RedisSerializer<String>) getRedisTemplate().getKeySerializer();
	}

	@SuppressWarnings("all")
	private static RedisSerializer<String> getValueSerializer() {
		return (RedisSerializer<String>) getRedisTemplate().getValueSerializer();
	}

	public static HashOperations<String, String, String> hashOps() {
		return getRedisTemplate().opsForHash();
	}

	public static ValueOperations<String, String> valueOps() {
		return getRedisTemplate().opsForValue();
	}

	public static ListOperations<String, String> listOps() {
		return getRedisTemplate().opsForList();
	}

	public static SetOperations<String, String> setOps() {
		return getRedisTemplate().opsForSet();
	}

	public static ZSetOperations<String, String> zSetOps() {
		return getRedisTemplate().opsForZSet();
	}

	public static StreamOperations<String, String, String> streamOps() {
		return getRedisTemplate().opsForStream();
	}

	// --------------------- key command start -----------------

	/**
	 * 删除指定的 key
	 * @param key 要删除的 key
	 * @return 删除成功返回 true, 如果 key 不存在则返回 false
	 * @see <a href="http://redis.io/commands/del">Del Command</a>
	 */
	public static boolean del(String key) {
		return Boolean.TRUE.equals(getRedisTemplate().delete(key));
	}

	/**
	 * 删除指定的 keys
	 * @param keys 要删除的 key 数组
	 * @return 如果删除了一个或多个 key，则为大于 0 的整数，如果指定的 key 都不存在，则为 0
	 */
	public static long del(String... keys) {
		return del(Arrays.asList(keys));
	}

	public static long del(Collection<String> keys) {
		Long deleteNumber = getRedisTemplate().delete(keys);
		return deleteNumber == null ? 0 : deleteNumber;
	}

	/**
	 * 判断 key 是否存在
	 * @param key 待判断的 key
	 * @return 如果 key 存在 {@code true} , 否则返回 {@code false}
	 * @see <a href="http://redis.io/commands/exists">Exists Command</a>
	 */
	public static boolean exists(String key) {
		return Boolean.TRUE.equals(getRedisTemplate().hasKey(key));
	}

	/**
	 * 判断指定的 key 是否存在.
	 * @param keys 待判断的数组
	 * @return 指定的 keys 在 redis 中存在的的数量
	 * @see <a href="http://redis.io/commands/exists">Exists Command</a>
	 */
	public static long exists(String... keys) {
		return exists(Arrays.asList(keys));
	}

	public static long exists(Collection<String> keys) {
		Long number = getRedisTemplate().countExistingKeys(keys);
		return number == null ? 0 : number;
	}

	/**
	 * 设置过期时间
	 * @param key 待修改过期时间的 key
	 * @param timeout 过期时长，单位 秒
	 * @see <a href="http://redis.io/commands/expire">Expire Command</a>
	 */
	public static boolean expire(String key, long timeout) {
		return expire(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置过期时间
	 * @param key 待修改过期时间的 key
	 * @param timeout 时长
	 * @param timeUnit 时间单位
	 */
	public static boolean expire(String key, long timeout, TimeUnit timeUnit) {
		return Boolean.TRUE.equals(getRedisTemplate().expire(key, timeout, timeUnit));
	}

	/**
	 * 设置 key 的过期时间到指定的日期
	 * @param key 待修改过期时间的 key
	 * @param date 过期时间
	 * @return 修改成功返回 true
	 * @see <a href="https://redis.io/commands/expireat/">ExpireAt Command</a>
	 */
	public static boolean expireAt(String key, Date date) {
		return Boolean.TRUE.equals(getRedisTemplate().expireAt(key, date));
	}

	public static boolean expireAt(String key, Instant expireAt) {
		return Boolean.TRUE.equals(getRedisTemplate().expireAt(key, expireAt));
	}

	/**
	 * 获取所有符合指定表达式的 key
	 * @param pattern 表达式
	 * @return java.util.Set<java.lang.String>
	 * @see <a href="http://redis.io/commands/keys">Keys Command</a>
	 */
	public static Set<String> keys(String pattern) {
		return getRedisTemplate().keys(pattern);
	}

	/**
	 * TTL 命令返回 {@link RedisHelper#expire(String, long) EXPIRE} 命令设置的剩余生存时间（以秒为单位）.。
	 * <p>
	 * 时间复杂度: O(1)
	 * @param key 待查询的 key
	 * @return TTL 以秒为单位，或负值以指示错误
	 * @see <a href="http://redis.io/commands/ttl">TTL Command</a>
	 */
	public static long ttl(String key) {
		return getRedisTemplate().getExpire(key);
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param scanOptions scan 的配置
	 * @return Cursor，一个可迭代对象
	 * @see <a href="https://redis.io/commands/scan/">Scan Command</a>
	 */
	public static Cursor<String> scan(ScanOptions scanOptions) {
		return getRedisTemplate().scan(scanOptions);
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param patten key 的规则
	 * @return Cursor，一个可迭代对象
	 */
	public static Cursor<String> scan(String patten) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(patten).build();
		return scan(scanOptions);
	}

	/**
	 * 使用 Cursor 遍历指定规则的 keys
	 * @param patten key 的规则
	 * @param count 一次扫描获取的 key 数量， 默认为 10
	 * @return Cursor，一个可迭代对象
	 * @see <a href="https://redis.io/commands/scan/">Scan Command</a>
	 */
	public static Cursor<String> scan(String patten, long count) {
		ScanOptions scanOptions = ScanOptions.scanOptions().match(patten).count(count).build();
		return scan(scanOptions);
	}
	// ====================== key command end ==================

	// ---------------------- string command start ---------------

	/**
	 * 当 key 存在时，对其值进行自减操作 （自减步长为 1），当 key 不存在时，则先赋值为 0 再进行自减
	 * @param key key
	 * @return 自减之后的 value 值
	 * @see #decrBy(String, long)
	 */
	public static long decr(String key) {
		return valueOps().decrement(key);
	}

	/**
	 * 当 key 存在时，对其值进行自减操作，当 key 不存在时，则先赋值为 0 再进行自减
	 * @param key key
	 * @param delta 自减步长
	 * @return 自减之后的 value 值
	 * @see <a href="http://redis.io/commands/decrby">DecrBy Command</a>
	 */
	public static long decrBy(String key, long delta) {
		return valueOps().decrement(key, delta);
	}

	/**
	 * 获取指定 key 的 value 值
	 * @param key 指定的 key
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/get">Get Command</a>
	 */
	public static String get(String key) {
		return valueOps().get(key);
	}

	/**
	 * 获取指定 key 的 value 值，并将指定的 key 进行删除
	 * @param key 指定的 key
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getdel/">GetDel Command</a>
	 */
	public static String getDel(String key) {
		return valueOps().getAndDelete(key);
	}

	/**
	 * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
	 * @param key 指定的 key
	 * @param timeout 过期时间，单位时间秒
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getex/">GetEx Command</a>
	 */
	public static String getEx(String key, long timeout) {
		return getEx(key, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 获取指定 key 的 value 值，并对 key 设置指定的过期时间
	 * @param key 指定的 key
	 * @param timeout 过期时间，单位时间秒
	 * @param timeUnit 时间单位
	 * @return 当 key 不存在时返回 null
	 * @see <a href="http://redis.io/commands/getex/">GetEx Command</a>
	 */
	public static String getEx(String key, long timeout, TimeUnit timeUnit) {
		return valueOps().getAndExpire(key, timeout, timeUnit);
	}

	/**
	 * 获取指定的 key 的 value 值，并同时使用指定的 value 值进行覆盖操作
	 * @param key 指定的 key
	 * @param value 新的 value 值
	 * @return 当 key 存在时返回其 value 值，否则返回 null
	 * @see <a href="http://redis.io/commands/getset">GetSet Command</a>
	 */
	public static String getSet(String key, String value) {
		return valueOps().getAndSet(key, value);
	}

	/**
	 * 对 key 进行自增，自增步长为 1
	 * @param key 需要自增的 key
	 * @return 自增后的 value 值
	 * @see #incrBy(String, long)
	 */
	public static long incr(String key) {
		return valueOps().increment(key);
	}

	/**
	 * 对 key 进行自增 (步长为 1)，并设置过期时间
	 * @see #incrByAndExpire(String, long, long)
	 */
	public static long incrAndExpire(String key, long timeout) {
		return incrByAndExpire(key, 1, timeout);
	}

	/**
	 * 对 key 进行自增，并指定自增步长, 当 key 不存在时先创建一个值为 0 的 key，再进行自增
	 * @param key 需要自增的 key
	 * @param delta 自增的步长
	 * @return 自增后的 value 值
	 * @see <a href="http://redis.io/commands/incrby">IncrBy Command</a>
	 */
	public static long incrBy(String key, long delta) {
		return valueOps().increment(key, delta);
	}

	/**
	 * 对 key 进行自增并设置过期时间，指定自增步长, 当 key 不存在时先创建一个值为 0 的 key，再进行自增
	 * @param key 需要自增的 key
	 * @param delta 自增的步长
	 * @param timeout 过期时间（单位：秒）
	 * @return 自增后的 value 值
	 */
	public static long incrByAndExpire(String key, long delta, long timeout) {
		return getRedisTemplate().execute(INCR_BY_EXPIRE_LUA_SCRIPT, Collections.singletonList(key),
				String.valueOf(delta), String.valueOf(timeout));
	}

	/**
	 * @see #incrBy(String, long)
	 */
	public static double incrByFloat(String key, double delta) {
		return valueOps().increment(key, delta);
	}

	/**
	 * 从指定的 keys 批量获取 values
	 * @param keys keys
	 * @return values list，当值为空时，该 key 对应的 value 为 null
	 * @see <a href="http://redis.io/commands/mget">MGet Command</a>
	 */
	public static List<String> mGet(Collection<String> keys) {
		return valueOps().multiGet(keys);
	}

	/**
	 * @see #mGet(Collection)
	 */
	public static List<String> mGet(String... keys) {
		return mGet(Arrays.asList(keys));
	}

	/**
	 * 批量获取 keys 的值，并返回一个 map
	 * @param keys keys
	 * @return map，key 和 value 的键值对集合，当 value 获取为 null 时，不存入此 map
	 */
	public static Map<String, String> mGetToMap(Collection<String> keys) {
		List<String> values = valueOps().multiGet(keys);
		Map<String, String> map = new HashMap<>(keys.size());
		if (values == null || values.isEmpty()) {
			return map;
		}

		Iterator<String> keysIterator = keys.iterator();
		Iterator<String> valuesIterator = values.iterator();
		while (keysIterator.hasNext()) {
			String key = keysIterator.next();
			String value = valuesIterator.next();
			if (value != null) {
				map.put(key, value);
			}
		}
		return map;
	}

	/**
	 * @see #mGetToMap(Collection)
	 */
	public static Map<String, String> mGetToMap(String... keys) {
		return mGetToMap(Arrays.asList(keys));
	}

	/**
	 * 设置 value for key
	 * @param key 指定的 key
	 * @param value 值
	 * @see <a href="https://redis.io/commands/set">Set Command</a>
	 */
	public static void set(String key, String value) {
		valueOps().set(key, value);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间 单位：秒
	 * @see #setEx(String, String, long)
	 */
	public static void set(String key, String value, long timeout) {
		set(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间 单位：秒
	 * @param timeUnit 过期时间单位
	 * @see #setEx(String, String, long,TimeUnit)
	 */
	public static void set(String key, String value, long timeout, TimeUnit timeUnit) {
		setEx(key, value, timeout, timeUnit);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key 指定的 key
	 * @param value 值
	 * @param timeout 过期时间
	 * @see <a href="https://redis.io/commands/setex">SetEx Command</a>
	 */
	public static void setEx(String key, String value, long timeout) {
		setEx(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 设置 value for key, 同时为其设置过期时间
	 * @param key 指定的 key
	 * @param value 值
	 * @param timeout 过期时间
	 * @param timeUnit 时间单位
	 * @see <a href="https://redis.io/commands/setex">SetEx Command</a>
	 */
	public static void setEx(String key, String value, long timeout, TimeUnit timeUnit) {
		valueOps().set(key, value, timeout, timeUnit);
	}

	/**
	 * 设置 value for key, 同时为其设置其在指定时间过期
	 * @param key key
	 * @param value value
	 * @param expireTime 在指定时间过期
	 */
	public static void setExAt(String key, String value, Instant expireTime) {
		long timeout = expireTime.getEpochSecond() - Instant.now().getEpochSecond();
		setEx(key, value, timeout);
	}

	/**
	 * 当 key 不存在时，进行 value 设置，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @return boolean
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public static boolean setNx(String key, String value) {
		return Boolean.TRUE.equals(valueOps().setIfAbsent(key, value));
	}

	/**
	 * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @return boolean 操作是否成功
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public static boolean setNxEx(String key, String value, long timeout) {
		return setNxEx(key, value, timeout, TimeUnit.SECONDS);
	}

	/**
	 * 当 key 不存在时，进行 value 设置并添加过期时间，当 key 存在时不执行操作
	 * @param key key
	 * @param value value
	 * @param timeout 过期时间
	 * @param timeUnit 时间单位
	 * @return boolean 操作是否成功
	 * @see <a href="https://redis.io/commands/setnx">SetNX Command</a>
	 */
	public static boolean setNxEx(String key, String value, long timeout, TimeUnit timeUnit) {
		return Boolean.TRUE.equals(valueOps().setIfAbsent(key, value, timeout, timeUnit));
	}

	// ----------------------- string command end -------------

	// ---------------------- hash command start ---------------
	/**
	 * 删除指定 hash 的 fields
	 * @param key hash 的 key
	 * @param fields hash 元素的 field 集合
	 * @return 删除的 field 数量
	 * @see <a href="https://redis.io/commands/hdel/">HDel Command</a>
	 */
	public static long hDel(String key, String... fields) {
		return hashOps().delete(key, (Object[]) fields);
	}

	/**
	 * 判断指定 hash 的 指定 field 是否存在
	 * @param key hash 的 key
	 * @param field 元素的 field
	 * @return 存在返回 {@code true}, 否则返回 {@code false}
	 * @see <a href="https://redis.io/commands/hexists/">HExists Command</a>
	 */
	public static boolean hExists(String key, String field) {
		return hashOps().hasKey(key, field);
	}

	/**
	 * 获取 hash 中的指定 field 对应的 value 值
	 * @param key hash 的 key
	 * @param field 元素的 field
	 * @see <a href="https://redis.io/commands/hget/">HGet Command</a>
	 */
	public static String hGet(String key, String field) {
		return hashOps().get(key, field);
	}

	/**
	 * 获取 hash 中所有的 fields 和 values, 并已键值对的方式返回
	 * @param key hash 的 key
	 * @see <a href="https://redis.io/commands/hgetall/">HGetAll Command</a>
	 */
	public static Map<String, String> hGetAll(String key) {
		return hashOps().entries(key);
	}

	/**
	 * 对 hash 中指定的 field 进行自增
	 * <p/>
	 * 若 field 不存在则，先设置为 0 再进行自增，若 hash 不存在则先创建 hash 再进行上述步骤
	 * @param key key
	 * @param field field
	 * @param delta 自增步长
	 * @return 自增后的 value 值
	 * @see <a href="https://redis.io/commands/hincrby/">HIncrBy Command</a>
	 */
	public static long hIncrBy(String key, String field, long delta) {
		return hashOps().increment(key, field, delta);
	}

	/**
	 * 对 hash 中指定的 field 进行自增
	 * @see #hIncrBy(String, String, long)
	 */
	public static Long hIncrBy(String key, String field) {
		return hIncrBy(key, field, 1);
	}

	/**
	 * 对 hash 中指定的 field 进行自增
	 * <p/>
	 * 若 field 不存在则，先设置为 0 再进行自增，若 hash 不存在则先创建 hash 再进行上述步骤
	 * @param key key
	 * @param field field
	 * @param delta 自增步长
	 * @return 自增后的 value 值
	 * @see <a href="https://redis.io/commands/hincrbyfloat/">HIncrByFloat Command</a>
	 */
	public static double hIncrByFloat(String key, String field, double delta) {
		return hashOps().increment(key, field, delta);
	}

	/**
	 * 返回 hash 中的所有 fields
	 * @param key hash 的 key
	 * @return Set of fields in hash
	 * @see <a href="https://redis.io/commands/hkeys/">HKeys Command</a>
	 */
	public static Set<String> hKeys(String key) {
		return hashOps().keys(key);
	}

	/**
	 * 返回 hash 中 fields 的数量
	 * @param key hash 的 key
	 * @return fields size
	 * @see <a href="https://redis.io/commands/hlen/">HLen Command</a>
	 */
	public static long hLen(String key) {
		return hashOps().size(key);
	}

	/**
	 * 返回 hash 中指定 fields 的值集合
	 * @param key hash 的 key
	 * @return fields value list, 按传入的 fields 顺序排列
	 * @see <a href="https://redis.io/commands/hkeys/">HKeys Command</a>
	 */
	public static List<String> hMGet(String key, Collection<String> fields) {
		return hashOps().multiGet(key, fields);
	}

	/**
	 * 返回 hash 中指定 fields 的值集合
	 * @param key hash 的 key
	 * @return fields value list, 按传入的 fields 顺序排列
	 * @see <a href="https://redis.io/commands/hkeys/">HKeys Command</a>
	 */
	public static List<String> hMGet(String key, String... fields) {
		return hashOps().multiGet(key, Arrays.asList(fields));
	}

	/**
	 * 修改 hash 中的 field 的值，有则覆盖，无则添加
	 * @param key hash 的 key
	 * @param field field
	 * @param value value
	 * @see <a href="https://redis.io/commands/hset/">HSet Command</a>
	 */
	public static void hSet(String key, String field, String value) {
		hashOps().put(key, field, value);
	}

	/**
	 * 修改 hash 中的 field 的值，有则不进行操作，无则添加
	 * @param key hash 的 key
	 * @param field field
	 * @param value value
	 * @see <a href="https://redis.io/commands/hsetnx/">HSetNx Command</a>
	 */
	public static void hSetNx(String key, String field, String value) {
		hashOps().putIfAbsent(key, field, value);
	}

	/**
	 * 返回 hash 中的所有 values
	 * @param key hash 的 key
	 * @return List of fields in hash
	 * @see <a href="https://redis.io/commands/hvals/">HVals Command</a>
	 */
	public static List<String> hVals(String key) {
		return hashOps().values(key);
	}

	// -------------------------- hash command end --------------------------------

	// -------------------------- list command start --------------------------------

	/**
	 * 获取指定 list 指定索引位置的元素
	 * @param key list 的 key
	 * @param index 索引位置，0 表示第一个元素，负数索引用于指定从尾部开始计数，-1 表示最后一个元素，-2 倒数第二个
	 * @return 返回对应索引位置的元素，不存在时为 null
	 * @see <a href="https://redis.io/commands/lindex/">LIndex Command</a>
	 */
	public static String lIndex(String key, long index) {
		return listOps().index(key, index);
	}

	/**
	 * 获取指定 list 的元素个数即长度
	 * @param key list 的 key
	 * @return 返回 list 的长度，当 list 不存在时返回 0
	 * @see <a href="https://redis.io/commands/llen/">LLen Command</a>
	 */
	public static long lLen(String key) {
		return listOps().size(key);
	}

	/**
	 * 以原子方式返回并删除列表的第一个元素，例如列表包含元素 "a", "b", "c" LPOP 操作将返回 ”a“ 并将其删除，list 中元素变为 ”b“, "c"
	 * @param key list 的 key
	 * @return 返回弹出的元素
	 * @see <a href="https://redis.io/commands/lpop/">LPop Command</a>
	 */
	public static String lPop(String key) {
		return listOps().leftPop(key);
	}

	/**
	 * 以原子方式返回并删除列表的多个元素
	 * @param key list 的 key
	 * @param count 弹出的个数
	 * @return 返回弹出的元素列表，key 不存在时为 null
	 * @see <a href="https://redis.io/commands/lpop/">LPop Command</a>
	 * @since Redis 版本大于等于 6.2.0
	 */
	public static List<String> lPop(String key, long count) {
		return listOps().leftPop(key, count);
	}

	/**
	 * 该命令返回 list 匹配元素的索引。它会从头到尾扫描列表，寻找 “element” 的第一个匹配项。
	 * @param key list 的 key
	 * @param element 查找的元素
	 * @return 指定元素正向第一个匹配项的索引，如果找不到，返回 null
	 * @see <a href="https://redis.io/commands/lpos/">LPos Command</a>
	 * @since Redis 版本大于等于 6.0.6
	 */
	public static Long lPos(String key, String element) {
		return listOps().indexOf(key, element);
	}

	/**
	 * 将指定的元素插入 list 的头部，若 list 不存在，则先指向创建一个空的 list
	 * @param key list 的 key
	 * @param elements 插入的元素
	 * @return 插入后的 list 长度
	 * @see <a href="https://redis.io/commands/lpush/">LPush Command</a>
	 */
	public static long lPush(String key, String... elements) {
		return listOps().leftPushAll(key, elements);
	}

	/**
	 * 将指定的值插入 list 的头部，若 list 不存在，则先指向创建一个空的 list
	 * @param key list 的 key
	 * @param elements 插入的元素
	 * @return 插入后的 list 长度
	 * @see <a href="https://redis.io/commands/lpush/">LPush Command</a>
	 */
	public static long lPush(String key, List<String> elements) {
		return listOps().leftPushAll(key, elements);
	}

	/**
	 * 获取 list 指定 offset 间的元素。
	 * @param key list 的 key
	 * @param start begin offset, 从 0 开始，0 表示列表第一个元素，也可以为负数，表示从 list 末尾开始的偏移量， -1
	 * 是列表最后第一个元素
	 * @param end end offset，值规则 同 start
	 * @return 元素集合
	 * @see <a href="https://redis.io/commands/lrange/">LRange Command</a>
	 */
	public static List<String> lRange(String key, long start, long end) {
		return listOps().range(key, start, end);
	}

	/**
	 * 删除 list 中的元素
	 * <ul>
	 * <li><b>count > 0</b>: 从 list 头部向尾部查找并删除 n 个和指定值相等的元素，n 为 count
	 * <li><b>count < 0</b>: 从 list 尾部向头部查找并删除 n 个和指定值相等的元素，n 为 count 的绝对值
	 * <li><b>count = 0</b>: 删除 list 中所有和指定值相等的元素
	 * </ul>
	 * @param key list 的 key
	 * @param count 删除的数量以及规则
	 * @param value 待删除的元素值
	 * @return 移除元素的数量
	 * @see <a href="https://redis.io/commands/lrem/">LRem Command</a>
	 */
	public static long lRem(String key, long count, String value) {
		return listOps().remove(key, count, value);
	}

	/**
	 * 将 list 指定 index 位置的元素设置为当前值
	 * @param key list 的 key
	 * @param index 索引位置，0 表示第一个元素，负数索引用于指定从尾部开始计数，-1 表示最后一个元素，-2 倒数第二个
	 * @param value 值
	 * @see <a href="https://redis.io/commands/lset/">LSet Command</a>
	 */
	public static void lSet(String key, long index, String value) {
		listOps().set(key, index, value);
	}

	/**
	 * 裁剪 list，只保留 start 到 end 之间的元素值，包含 start 和 end
	 * @param key list 的 key
	 * @param start 开始索引位置，0 表示第一个元素，负数索引用于指定从尾部开始计数，-1 表示最后一个元素，-2 倒数第二个
	 * @param end 结束的索引位置
	 * @see <a href="https://redis.io/commands/ltrim/">LTrim Command</a>
	 */
	public static void lTrim(String key, long start, long end) {
		listOps().trim(key, start, end);
	}

	/**
	 * 以原子方式返回并删除列表的最后一个元素。
	 * <p>
	 * 例如 list 包含元素 "a"、"b"、"c", RPOP 操作将返回 ”c“ 并将其删除，list 中元素变为 ”a“, "b"
	 * @param key list 的 key
	 * @return 弹出的元素
	 * @see <a href="https://redis.io/commands/rpop/">RPOP Command</a>
	 */
	public static String rPop(String key) {
		return listOps().rightPop(key);
	}

	/**
	 * 从 list 尾部，以原子方式返回并删除列表中指定数量的元素。
	 * @param key list 的 key
	 * @param count 待弹出的元素数量
	 * @return 弹出的元素集合
	 * @see <a href="https://redis.io/commands/rpop/">RPOP Command</a>
	 * @since Redis 6.2.0
	 */
	public static List<String> rPop(String key, long count) {
		return listOps().rightPop(key, count);
	}

	/**
	 * 将指定的值插入 list 的尾部，若 list 不存在，则先指向创建一个空的 list
	 * @param key list 的 key
	 * @param values 插入的元素
	 * @return 插入后的 list 长度
	 * @see <a href="https://redis.io/commands/rpush/">RPush Command</a>
	 */
	public static long rPush(String key, String... values) {
		return listOps().rightPushAll(key, values);
	}

	/**
	 * 将指定的值插入 list 的尾部，若 list 不存在，则先指向创建一个空的 list
	 * @param key list 的 key
	 * @param values 插入的元素
	 * @return 插入后的 list 长度
	 * @see <a href="https://redis.io/commands/rpush/">RPush Command</a>
	 */
	public static long rPush(String key, List<String> values) {
		return listOps().rightPushAll(key, values);
	}

	// -------------------------- list command end --------------------------------

	// -------------------------- Set command start --------------------------------

	/**
	 * 将指定的 member 添加到 Set 中，如果 Set 中已有该 member 则忽略。如果 Set 不存在，则先创建一个新的 Set，再进行添加
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @param members 添加的成员
	 * @return 添加到集合中的元素数量，不包括集合中已经存在的所有元素
	 * @see <a href="https://redis.io/commands/sadd/">SAdd Command</a>
	 */
	public static long sAdd(String key, String... members) {
		return setOps().add(key, members);
	}

	/**
	 * 将指定的 member 添加到 Set 中，如果 Set 中已有该 member 则忽略。如果 Set 不存在，则先创建一个新的 Set，再进行添加
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @param members 添加的成员
	 * @return 添加到集合中的元素数量，不包括集合中已经存在的所有元素
	 * @see <a href="https://redis.io/commands/sadd/">SAdd Command</a>
	 */
	public static long sAdd(String key, List<String> members) {
		return setOps().add(key, members.toArray(new String[0]));
	}

	/**
	 * 返回 Set 中的元素数，如果 set 不存在则返回 0
	 * @param key Set 的 key
	 * @return The cardinality (number of elements) of the set
	 * @see <a href="https://redis.io/commands/scard/">SCard Command</a>
	 */
	public static long sCard(String key) {
		return setOps().size(key);
	}

	/**
	 * 判断指定的值是否是 Set 中的元素
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @param value 待判断的值
	 * @return 如果是 Set 中的元素返回{@code true}, 否则返回{@code false}
	 * @see <a href="https://redis.io/commands/sismember/">SIsMember Command</a>
	 */
	public static boolean sIsMember(String key, String value) {
		return setOps().isMember(key, value);
	}

	/**
	 * 获取 Set 中的所有元素
	 * <p>
	 * Time complexity O(N)
	 * @param key Set 的 key
	 * @return Set 中的所有元素
	 * @see <a href="https://redis.io/commands/smembers/">SMembers Command</a>
	 */
	public static Set<String> sMembers(String key) {
		return setOps().members(key);
	}

	/**
	 * 判断指定的值是否是 Set 中的元素
	 * <p>
	 * Time complexity O(N)
	 * @param key Set 的 key
	 * @param values 待判断的值集合
	 * @return 一个 Map, key 为待判断的值，value 为结果
	 * @see <a href="https://redis.io/commands/smismember/">SMIsMember Command</a>
	 * @since Redis 6.2.0
	 */
	public static Map<Object, Boolean> sMIsMember(String key, String... values) {
		return setOps().isMember(key, (Object[]) values);
	}

	/**
	 * 随机从 Set 中删除一个元素，并返回它，如果 Set 为空，则返回 null
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @return 弹出的元素，或者 null
	 * @see <a href="https://redis.io/commands/spop/">SPop Command</a>
	 */
	public static String sPop(String key) {
		return setOps().pop(key);
	}

	/**
	 * 随机从 Set 中返回一个元素，但不删除，如果 Set 为空，则返回 null
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @return 随机选中的元素或者 null
	 * @see <a href="https://redis.io/commands/srandmember/">SRandMember Command</a>
	 */
	public static String sRandMember(String key) {
		return setOps().randomMember(key);
	}

	/**
	 * 随机从 Set 中返回 count 个元素，但不删除，如果 Set 为空，则返回 null
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @param count 随机返回的元素数量
	 * @return 随机选中的元素或者 null
	 * @see <a href="https://redis.io/commands/srandmember/">SRandMember Command</a>
	 */
	public static Set<String> sRandMember(String key, long count) {
		return setOps().distinctRandomMembers(key, count);
	}

	/**
	 * 从 Set 中删除指定的 member，如果给的值不是 Set 的 member 则不进行操作
	 * <p>
	 * Time complexity O(1)
	 * @param key Set 的 key
	 * @param members 待删除的成员
	 * @return The number of members that were removed from the set, not including
	 * non-existing members
	 * @see <a href="https://redis.io/commands/srem/">SRem Command</a>
	 */
	public static long sRem(String key, String... members) {
		return setOps().remove(key, (Object[]) members);
	}

	/**
	 * 使用 Cursor 遍历指定 Set 中的所有元素
	 * @param scanOptions scan 的配置
	 * @return Cursor，一个可迭代对象
	 * @see <a href="https://redis.io/commands/sscan/">SScan Command</a>
	 */
	public static Cursor<String> sScan(String key, ScanOptions scanOptions) {
		return setOps().scan(key, scanOptions);
	}

	// -------------------------- Set command end --------------------------------

	// ---------------------- Sorted Set command start ----------------------------

	/**
	 * 添加拥有指定 score 的 member 到 Sorted Set 中。如果 member 在 Sorted Set 中已存在，则更新 score，并进行重排序。
	 * 如果 key 不存在，则先创建一个空的 Sorted Set 再进行添加操作。
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param score 分数
	 * @param member 成员
	 * @return 当元素被成功添加时返回 true，当元素存在时返回 false（分数会更新）
	 * @see <a href="https://redis.io/commands/zadd/">ZAdd Command</>
	 */
	public static boolean zAdd(String key, double score, String member) {
		return zSetOps().add(key, member, score);
	}

	/**
	 * 批量添加拥有指定 score 的 member 到 Sorted Set 中。如果 member 在 Sorted Set 中已存在，则更新
	 * score，并进行重排序。 如果 key 不存在，则先创建一个空的 Sorted Set 再进行添加操作。
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param scoreMembers 成员和分数的键值对
	 * @return 返回被成功添加的成员数
	 * @see <a href="https://redis.io/commands/zadd/">ZAdd Command</a>
	 */
	public static long zAdd(String key, Map<String, Double> scoreMembers) {
		Set<ZSetOperations.TypedTuple<String>> tuples = scoreMembers.entrySet()
			.stream()
			.map(x -> ZSetOperations.TypedTuple.of(x.getKey(), x.getValue()))
			.collect(Collectors.toSet());
		return zSetOps().add(key, tuples);
	}

	/**
	 * 返回 Sorted Set 的元素数量，若 key 不存在则返回 0
	 * <p>
	 * Time complexity O(1)
	 * @param key Sorted Set 的 key
	 * @return Sorted Set 中的元素数量
	 * @see <a href="https://redis.io/commands/zcard/">ZCard Command</a>
	 */
	public static long zCard(String key) {
		return zSetOps().size(key);
	}

	/**
	 * 如果 member 存在于 Sorted Set 中，则对其 score 和 increment 进行相加运算，并重排序。 <br/>
	 * 如果 member 不存在，则先添加一个 score 为 0 的 member 再进行相加操作。 <br/>
	 * 如果 key 不存在，则先创建一个 Sorted Set，再进行上述操作。
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param increment 增长步长，可以为负数
	 * @param member 成员
	 * @return The new score
	 * @see <a href="https://redis.io/commands/zincrby/">ZIncrBy Command</a>
	 */
	public static double zIncrBy(String key, double increment, String member) {
		return zSetOps().incrementScore(key, member, increment);
	}

	/**
	 * 返回并删除 Sorted Set 中分数最高的那个元素
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @return 弹出的 member 和 score
	 * @see <a href="https://redis.io/commands/zpopmax/">ZPopMax Command</a>
	 * @since Redis 5.0.0
	 */
	public static ZSetOperations.TypedTuple<String> zPopMax(String key) {
		return zSetOps().popMax(key);
	}

	/**
	 * 返回并删除 Sorted Set 中分数最高的 n 个元素
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param count 弹出的个数
	 * @return 弹出的 member 和 score
	 * @see <a href="https://redis.io/commands/zpopmax/">ZPopMax Command</a>
	 * @since Redis 5.0.0
	 */
	public static Set<ZSetOperations.TypedTuple<String>> zPopMax(String key, long count) {
		return zSetOps().popMax(key, count);
	}

	/**
	 * 返回并删除 Sorted Set 中分数最低的那个元素
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @return 弹出的 member 和 score
	 * @see <a href="https://redis.io/commands/zpopmin/">ZPopMin Command</a>
	 * @since Redis 5.0.0
	 */
	public static ZSetOperations.TypedTuple<String> zPopMin(String key) {
		return zSetOps().popMin(key);
	}

	/**
	 * 返回并删除 Sorted Set 中分数最低的 n 个元素
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param count 弹出的个数
	 * @return 弹出的 member 和 score
	 * @see <a href="https://redis.io/commands/zpopmin/">ZPopMin Command</a>
	 * @since Redis 5.0.0
	 */
	public static Set<ZSetOperations.TypedTuple<String>> zPopMin(String key, long count) {
		return zSetOps().popMin(key, count);
	}

	/**
	 * 随机从 Sorted Set 中返回一个 member
	 * <p>
	 * Time complexity O(N) where N is the number of elements returned
	 * @param key Sorted Set 的 Key
	 * @return Random String from the set
	 * @see <a href="https://redis.io/commands/zrandmember/">ZRandMember Command</a>
	 * @since Redis 6.2.0
	 */
	public static String zRandMember(String key) {
		return zSetOps().randomMember(key);
	}

	/**
	 * 返回 Sorted Set 中指定索引范围内的 member.
	 * <p>
	 * Time complexity O(log(N)+M) with N being the number of elements in the sorted set
	 * and M the number of elements returned.
	 * @param key the key to query
	 * @param start the minimum index
	 * @param end the maximum index
	 * @return A Set of Strings in the specified range
	 * @see <a href="https://redis.io/commands/zrange/">ZRange Command</a>
	 */
	public static Set<String> zRange(String key, long start, long end) {
		return zSetOps().range(key, start, end);
	}

	/**
	 * 返回 Sorted Set 中指定 score 间的所有元素（包括 score 等于 min 和 max 的元素）
	 * <p>
	 * Time complexity O(log(N)+M) with N being the number of elements in the sorted set
	 * and M the number of elements being returned.
	 * @param key the key to query
	 * @param min minimum score
	 * @param max maximum score
	 * @return A List of elements in the specified score range
	 * @see <a href="https://redis.io/commands/zrangebyscore/">ZRangeByScore Command</a>
	 */
	public static Set<String> zRangeByScore(String key, double min, double max) {
		return zSetOps().rangeByScore(key, min, max);
	}

	/**
	 * 返回 Sorted Set 中指定 score 间的所有元素（包括 score 等于 min 和 max 的元素）
	 * @param key the key to query
	 * @param min minimum score
	 * @param max maximum score
	 * @param offset 偏移量
	 * @param count 获取的元素数
	 * @return A List of elements in the specified score range
	 * @see <a href="https://redis.io/commands/zrangebyscore/">ZRangeByScore Command</a>
	 */
	public static Set<String> zRangeByScore(String key, double min, double max, long offset, long count) {
		return zSetOps().rangeByScore(key, min, max, offset, count);
	}

	/**
	 * 返回 Sorted Set 中指定 score 间的所有元素和其分数（包括 score 等于 min 和 max 的元素）
	 * @param key the key to query
	 * @param min minimum score
	 * @param max maximum score
	 * @return A List of elements in the specified score range
	 * @see <a href="https://redis.io/commands/zrangebyscore/">ZRangeByScore Command</a>
	 */
	public static Set<ZSetOperations.TypedTuple<String>> zRangeByScoreWithScores(String key, double min, double max) {
		return zSetOps().rangeByScoreWithScores(key, min, max);
	}

	/**
	 * 返回 member 的排名（索引）。排名从 0 开始，按分数从低到高的顺序.
	 * <p>
	 * Time complexity O(log(N))
	 * @param key Sorted Set 的 key
	 * @param member 成员
	 * @return 如果 member 存在的话返回其排名，否则返回 null
	 * @see <a href="https://redis.io/commands/zrank/">ZRank Command</a>
	 */
	public static Long zRank(String key, String member) {
		return zSetOps().rank(key, member);
	}

	/**
	 * 从 Sorted Set 中删除指定的 member。不存在的 member 将被忽略。
	 * <p>
	 * Time complexity O(log(N)) with N being the number of elements in the sorted set
	 * @param key Sorted Set 的 key
	 * @param members 待删除的成员
	 * @return 从排序集中删除的 member 数，不包括不存在的 member 数
	 * @see <a href="https://redis.io/commands/zrem/">ZRem Command</a>
	 */
	public static long zRem(String key, String... members) {
		return zSetOps().remove(key, (Object[]) members);
	}

	/**
	 * 返回 Sorted Set 中 index 在 start 和 end 之前的所有成员（包括 start 和 end）。
	 * <p>
	 * 与默认的排序规则相反，元素的顺序是按分数从高到低进行的，具有相同分数的元素以相反的字典顺序排序
	 * <p>
	 * Time complexity O(log(N)+M) with N being the number of elements in the sorted set
	 * and M the number of elements returned.
	 * @param key the key to query
	 * @param start the minimum index
	 * @param end the maximum index
	 * @return A List of Strings in the specified range
	 * @see <a href="https://redis.io/commands/zrevrange/">ZRevRange Commad</a>
	 */
	public static Set<String> zRevRange(String key, long start, long end) {
		return zSetOps().reverseRange(key, start, end);
	}

	/**
	 * 返回 Sorted Set 中分数在 min 和 max 之前的所有成员（包括 min 和 max）。
	 * <p>
	 * 与默认的排序规则相反，元素的顺序是按分数从高到低进行的。
	 * <p>
	 * 具有相同分数的元素以相反的字典顺序返回.
	 * <p>
	 * Time complexity O(log(N)+M) with N being the number of elements in the sorted set
	 * and M the number of elements being returned.
	 * @param key the key to query
	 * @param min minimum score
	 * @param max maximum score
	 * @return A List of elements in the specified score range
	 * @see <a href="https://redis.io/commands/zrevrangebyscore/">ZRevRangeByScore
	 * Commad</a>
	 */
	public static Set<String> zRevRangeByScore(String key, double min, double max) {
		return zSetOps().reverseRangeByScore(key, min, max);
	}

	/**
	 * 返回 Sorted Set 中指定 member 的分数。如果指定的 member 在 Sorted Set 中不存在，或者 Key 根本不存在，则返回 null。
	 * <p>
	 * <b>Time complexity:</b> O(1)
	 * @param key Sorted Set Key
	 * @param member Sorted Set Member
	 * @return the score
	 * @see <a href="https://redis.io/commands/zscore/">ZSCORE Commad</a>
	 */
	public static Double zScore(String key, String member) {
		return zSetOps().score(key, member);
	}

	// -------------------------------- lua 脚本 --------------------------
	/**
	 * 执行 lua脚本
	 * @param action redis 操作
	 * @return T
	 */
	public static <T> T execute(RedisCallback<T> action) {
		return getRedisTemplate().execute(action);
	}

	@Nullable
	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection) {
		return execute(action, exposeConnection, false);
	}

	@Nullable
	public static <T> T execute(RedisCallback<T> action, boolean exposeConnection, boolean pipeline) {
		return getRedisTemplate().execute(action, exposeConnection, pipeline);
	}

	public static <T> T execute(SessionCallback<T> session) {
		return getRedisTemplate().execute(session);
	}

	public static <T> T execute(RedisScript<T> script, List<String> keys, Object... args) {
		return getRedisTemplate().execute(script, keys, args);
	}

	public static <T> T execute(RedisScript<T> script, RedisSerializer<?> argsSerializer,
			RedisSerializer<T> resultSerializer, List<String> keys, Object... args) {
		return getRedisTemplate().execute(script, argsSerializer, resultSerializer, keys, args);
	}

	// ----------------------- pipelined 操作 --------------------

	public static List<Object> executePipelined(SessionCallback<?> session) {
		return getRedisTemplate().executePipelined(session);
	}

	public static List<Object> executePipelined(SessionCallback<?> session,
			@Nullable RedisSerializer<?> resultSerializer) {
		return getRedisTemplate().executePipelined(session, resultSerializer);
	}

	public static List<Object> executePipelined(RedisCallback<?> action) {
		return getRedisTemplate().executePipelined(action);
	}

	public static List<Object> executePipelined(RedisCallback<?> action,
			@Nullable RedisSerializer<?> resultSerializer) {
		return getRedisTemplate().executePipelined(action, resultSerializer);
	}

	// =================== PUB/SUB command start =================

	/**
	 * Publish 一条消息
	 * @param channel 渠道
	 * @param message 消息
	 */
	public static void publish(String channel, String message) {
		getRedisTemplate().convertAndSend(channel, message);
	}

	/**
	 * Publish 一条消息
	 * @param channel 渠道
	 * @param message 消息
	 */
	public static void publish(String channel, byte[] message) {
		getRedisTemplate().convertAndSend(channel, message);
	}

	// =================== PUB/SUB command end =================

	// =================== Stream command start =================

	/**
	 * XACK key group ID [ID ...]
	 * @param key key of stream
	 * @param group consume group
	 * @param ids record ids
	 * @see <a href="https://redis.io/commands/xack/">XACK Command</a>
	 * @since Redis 5.0.0
	 */
	public static long xAck(String key, String group, String... ids) {
		return streamOps().acknowledge(key, group, ids);
	}

	public static long xAck(String key, String group, RecordId... ids) {
		return streamOps().acknowledge(key, group, ids);
	}

	/**
	 * XADD key ID field string [field string ...]
	 * @param key key of stream
	 * @param content record content
	 * @return the ID of the added entry
	 * @see <a href="https://redis.io/commands/xadd/">XADD Command</a>
	 * @since Redis 5.0.0
	 */
	public static RecordId xAdd(String key, Map<String, String> content) {
		return streamOps().add(StreamRecords.newRecord().in(key).ofMap(content));
	}

	public static RecordId xAdd(String key, Map<String, String> content, RedisStreamCommands.XAddOptions xAddOptions) {
		return xAdd(Record.of(content).withStreamKey(key), xAddOptions);
	}

	public static RecordId xAdd(MapRecord<String, String, String> mapRecord,
			RedisStreamCommands.XAddOptions xAddOptions) {
		RedisSerializer<String> keySerializer = getKeySerializer();
		RedisSerializer<String> valueSerializer = getValueSerializer();

		byte[] rawKey = keySerializer.serialize(mapRecord.getStream());

		Map<String, String> content = mapRecord.getValue();
		Map<byte[], byte[]> rawContent = new LinkedHashMap<>(content.size());

		for (Map.Entry<String, String> entry : content.entrySet()) {
			rawContent.put(keySerializer.serialize(entry.getKey()), valueSerializer.serialize(entry.getValue()));
		}

		return getRedisTemplate().execute((RedisConnection conn) -> conn.streamCommands()
			.xAdd(Record.of(rawContent).withStreamKey(rawKey), xAddOptions));
	}

	/**
	 * XDEL key ID [ID ...]
	 * @param key key of stream
	 * @param ids record ids
	 * @see <a href="https://redis.io/commands/xdel/">XDEL Command</a>
	 * @since Redis 5.0.0
	 */
	public static long xDel(String key, String... ids) {
		return streamOps().delete(key, ids);
	}

	public static long xDel(String key, RecordId... ids) {
		return streamOps().delete(key, ids);
	}

	/**
	 * XGROUP CREATE <key> <groupName> <id or $>
	 * @param key key of stream
	 * @param groupName group name
	 * @see <a href="https://redis.io/commands/xgroup-create/">XGROUP CREATE Command</a>
	 * @since Redis 5.0.0
	 */
	public static String xGroupCreate(String key, String groupName, ReadOffset readOffset, boolean makeStream) {
		RedisSerializer<String> keySerializer = getKeySerializer();
		byte[] rawKey = keySerializer.serialize(key);

		return getRedisTemplate().execute((RedisConnection conn) -> conn.streamCommands()
			.xGroupCreate(rawKey, groupName, readOffset, makeStream));
	}

	public static String xGroupCreate(String key, String groupName) {
		return xGroupCreate(key, groupName, ReadOffset.latest(), true);
	}

	/**
	 * XLEN key
	 * @param key key of stream
	 * @return length of stream
	 * @see <a href="https://redis.io/commands/xlen/">XLEN Command</a>
	 * @since Redis 5.0.0
	 */
	public static long xLen(String key) {
		return streamOps().size(key);
	}

	/**
	 * XRANGE key start end COUNT count
	 * @param key key of stream
	 * @param range start and end
	 * @return The entries with IDs matching the specified range.
	 * @see <a href="https://redis.io/commands/xrange/">XRANGE Command</a>
	 * @since Redis 5.0.0
	 */
	public static List<MapRecord<String, String, String>> xRange(String key, Range<String> range) {
		return streamOps().range(key, range);
	}

	public static List<MapRecord<String, String, String>> xRange(String key, Range<String> range,
			RedisZSetCommands.Limit limit) {
		return streamOps().range(key, range, limit);
	}

	/**
	 * XREAD [COUNT count] [BLOCK milliseconds] STREAMS key [key ...] ID [ID ...]
	 *
	 * @see <a href="https://redis.io/commands/xread/">XREAD Command</a>
	 * @since Redis 5.0.0
	 */
	public static List<MapRecord<String, String, String>> xRead(StreamOffset<String>... streams) {
		return streamOps().read(streams);
	}

	public static List<MapRecord<String, String, String>> xRead(StreamReadOptions streamReadOptions,
			StreamOffset<String>... streams) {
		return streamOps().read(streamReadOptions, streams);
	}

	/**
	 * XREADGROUP GROUP group consumer [COUNT count] [BLOCK milliseconds] [NOACK] STREAMS
	 * key [key ...] id [id ...]
	 *
	 * @see <a href="https://redis.io/commands/xreadgroup/">XREADGROUP Command</a>
	 * @since Redis 5.0.0
	 */
	public static List<MapRecord<String, String, String>> xReadGroup(Consumer consumer,
			StreamOffset<String>... streams) {
		return streamOps().read(consumer, streams);
	}

	public static List<MapRecord<String, String, String>> xReadGroup(Consumer consumer,
			StreamReadOptions streamReadOptions, StreamOffset<String>... streams) {
		return streamOps().read(consumer, streamReadOptions, streams);
	}

	public static List<MapRecord<String, String, String>> xReadGroup(String group, String consumer,
			StreamOffset<String>... streams) {
		return streamOps().read(Consumer.from(group, consumer), streams);
	}

	public static List<MapRecord<String, String, String>> xReadGroup(String group, String consumer,
			StreamReadOptions streamReadOptions, StreamOffset<String>... streams) {
		return streamOps().read(Consumer.from(group, consumer), streamReadOptions, streams);
	}

}
