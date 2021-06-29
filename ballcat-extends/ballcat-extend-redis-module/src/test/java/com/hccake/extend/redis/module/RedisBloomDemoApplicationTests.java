package com.hccake.extend.redis.module;

import com.hccake.ballcat.common.redis.serialize.PrefixStringRedisSerializer;
import com.hccake.extend.redis.moudle.bloom.BloomInsertOptions;
import com.hccake.extend.redis.moudle.bloom.BloomRedisModuleHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.List;
import java.util.Map;

@Slf4j
class RedisBloomDemoApplicationTests {

	BloomRedisModuleHelper bloomRedisModuleHelper;

	private static final String redisHost = "192.168.1.3";

	private static final int redisPort = 20208;

	/**
	 * 可自行替换为集群配置
	 */
	@BeforeEach
	public void init() {
		// 获取 Lettuce 链接工厂
		LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
		lettuceConnectionFactory.afterPropertiesSet();
		// 获取布隆过滤器操作助手
		bloomRedisModuleHelper = new BloomRedisModuleHelper(lettuceConnectionFactory);
		// 可选操作：配合 ballcat-spring-boot-starter-redis 提供的 PrefixStringRedisSerializer，可以给
		// redis key 添加默认的 key 前缀
		bloomRedisModuleHelper.setKeySerializer(new PrefixStringRedisSerializer("keyprefix:"));
	}

	@Test
	void commandTest() {
		String filterKey = "TEST_FILTER";

		// 1.创建布隆过滤器
		boolean create = bloomRedisModuleHelper.createFilter(filterKey, 0.01, 1000000000);
		log.info("test createFilter result: {}", create);

		// 2.添加一个元素
		Boolean foo = bloomRedisModuleHelper.add(filterKey, "foo");
		log.info("test add result: {}", foo);

		// 3.批量添加元素
		List<Boolean> addMulti = bloomRedisModuleHelper.multiAdd(filterKey, "foo", "bar");
		log.info("test addMulti result: {}", addMulti);

		// 4.校验一个元素是否存在
		Boolean existsFoo = bloomRedisModuleHelper.exists(filterKey, "foo");
		log.info("test existsFoo result: {}", existsFoo);

		Boolean existsBar = bloomRedisModuleHelper.exists(filterKey, "bar");
		log.info("test existsBar result: {}", existsBar);

		// 5.批量校验元素是否存在
		List<Boolean> existsMulti = bloomRedisModuleHelper.multiExists(filterKey, "foo", "foo1");
		log.info("test existsMulti result: {}", existsMulti);

		// 6.获取 filter info
		Map<String, Object> info = bloomRedisModuleHelper.info(filterKey);
		log.info("test info result: {}", info);

		// 7.删除布隆过滤器
		Boolean delete = bloomRedisModuleHelper.delete(filterKey);
		log.info("test delete result: {}", delete);
	}

	@Test
	void insertTest() {
		String filterKey = "INSERT_TEST_FILTER";
		// 1. 定义 filter 属性
		BloomInsertOptions insertOptions = new BloomInsertOptions().capacity(1000).error(0.001);

		// 2. 判断元素是否存在
		List<Boolean> existsMulti1 = bloomRedisModuleHelper.multiExists(filterKey, "foo", "foo3", "foo5");
		log.info("test existsMulti1 result: {}", existsMulti1);

		// 3. 插入部分数据
		List<Boolean> insert1 = bloomRedisModuleHelper.insert(filterKey, insertOptions, "foo1", "foo2", "foo3");
		log.info("test insert1 result: {}", insert1);

		// 4. 再次执行 insert 进行插入
		List<Boolean> insert2 = bloomRedisModuleHelper.insert(filterKey, insertOptions, "foo2", "foo3", "foo4");
		log.info("test insert2 result: {}", insert2);

		// 5. 再次判断元素是否存在
		List<Boolean> existsMulti2 = bloomRedisModuleHelper.multiExists(filterKey, "foo", "foo3", "foo4", "foo5");
		log.info("test existsMulti2 result: {}", existsMulti2);

		// 6.获取 filter info
		Map<String, Object> info = bloomRedisModuleHelper.info(filterKey);
		log.info("test info result: {}", info);

		// 7.删除布隆过滤器
		Boolean delete = bloomRedisModuleHelper.delete(filterKey);
		log.info("test delete result: {}", delete);
	}

}
