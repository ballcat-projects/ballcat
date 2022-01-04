package com.hccake.extend.redis.moudle.bloom;

import com.hccake.extend.redis.moudle.AbstractRedisModuleHelper;
import io.lettuce.core.output.ArrayOutput;
import io.lettuce.core.output.BooleanListOutput;
import io.lettuce.core.output.BooleanOutput;
import io.lettuce.core.protocol.CommandType;
import org.springframework.data.redis.connection.lettuce.BloomCommandEnum;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * redisBloom module 的操作帮助类
 *
 * @author hccake
 */
public class BloomRedisModuleHelper extends AbstractRedisModuleHelper {

	public BloomRedisModuleHelper(LettuceConnectionFactory connectionFactory) {
		super(connectionFactory);
	}

	/**
	 * 获得一个全部值都是 false 的 Boolean List
	 * @param size list size
	 * @return List<Boolean>
	 */
	private List<Boolean> getAllFalseBooleanList(long size) {
		return Stream.iterate(false, UnaryOperator.identity()).limit(size).collect(Collectors.toList());
	}

	/**
	 * 创建一个 bloomFilter
	 * @param key The key of the filter
	 * @param errorRate 期望错误率
	 * @param initCapacity 初始容量
	 * @return 成功返回 true，失败返回 false
	 */
	public boolean createFilter(String key, double errorRate, long initCapacity) {
		return execute(key, BloomCommandEnum.RESERVE, new BooleanOutput<>(codec), String.valueOf(errorRate),
				String.valueOf(initCapacity)).orElse(false);
	}

	/**
	 * 向 bloomFilter 中添加一个元素，如果 bloomFilter 不存在，则创建一个 bloomFilter
	 * @param key The key of the filter
	 * @param item 需要添加的元素
	 * @return 如果元素不在过滤器中，则可以添加成功，返回 true
	 */
	public boolean add(String key, String item) {
		return execute(key, BloomCommandEnum.ADD, new BooleanOutput<>(codec), item).orElse(false);
	}

	/**
	 * 批量添加元素到 bloomFilter 中，如果 bloomFilter 不存在，则创建一个 bloomFilter
	 * @param key The key of the filter
	 * @param items 需要添加的元素数组
	 * @return 一个长度与值的个数相同的布尔集合。 每个布尔值指示相应的元素之前是否在过滤器中。 一个真值意味着该元素以前不存在，而一个假值意味着它以前可能存在。
	 */
	public List<Boolean> multiAdd(String key, String... items) {
		Optional<List<Boolean>> result = execute(key, BloomCommandEnum.MADD, new BooleanListOutput<>(codec), items);
		return result.orElseGet(() -> getAllFalseBooleanList(items.length));
	}

	/**
	 * 判断元素是否在 bloomFilter 中
	 * @param key The key of the filter
	 * @param item 被检查的元素
	 * @return 如果该项目在筛选器中存在，则为 true; 如果该项目在筛选器中不存在，则为false
	 */
	public boolean exists(String key, String item) {
		return execute(key, BloomCommandEnum.EXISTS, new BooleanOutput<>(codec), item).orElse(false);
	}

	/**
	 * 判断一个或多个元素是否在 bloomFilter 中
	 * @param key The key of the filter
	 * @param items 被检查的元素数组
	 * @return 一个布尔集合。 true表示对应的值可能存在，false表示不存在
	 */
	public List<Boolean> multiExists(String key, String... items) {
		Optional<List<Boolean>> result = execute(key, BloomCommandEnum.MEXISTS, new BooleanListOutput<>(codec), items);
		return result.orElseGet(() -> getAllFalseBooleanList(items.length));
	}

	/**
	 * 添加一个元素到 bloomFilter，默认情况下，如果 bloomFilter 不存在就创建它 (根据 options 决定)
	 * @param key The key of the filter
	 * @param bloomInsertOptions 插入选项 {@link BloomInsertOptions}
	 * @param item 待添加的元素
	 * @return true 标识元素不在过滤器中，false 表示已存在该元素
	 */
	public Boolean insertOne(String key, BloomInsertOptions bloomInsertOptions, String item) {
		List<Boolean> list = insert(key, bloomInsertOptions, item);
		return list.get(0);
	}

	/**
	 * 添加一个或多个元素到 bloomFilter，默认情况下，如果 bloomFilter 不存在就创建它 (根据 options 决定)
	 * @param key The key of the filter
	 * @param bloomInsertOptions 插入选项 {@link BloomInsertOptions}
	 * @param items 待添加的元素数组
	 * @return 一个布尔集合。 true 标识元素不在过滤器中，false 表示已存在该元素
	 */
	public List<Boolean> insert(String key, BloomInsertOptions bloomInsertOptions, String... items) {
		final List<String> args = new ArrayList<>(bloomInsertOptions.getOptions());
		args.add(BloomInsertKeywordEnum.ITEMS.name());
		Collections.addAll(args, items);

		Optional<List<Boolean>> result = execute(key, BloomCommandEnum.INSERT, new BooleanListOutput<>(codec),
				args.toArray(new String[0]));
		return result.orElseGet(() -> getAllFalseBooleanList(items.length));
	}

	/**
	 * 获取 bloomFilter 的相关信息
	 * @param key The key of the filter
	 * @return Return information
	 */
	public Map<String, Object> info(String key) {
		Optional<List<Object>> result = execute(key, BloomCommandEnum.INFO, new ArrayOutput<>(codec));
		List<Object> values = result.orElseGet(ArrayList::new);

		Map<String, Object> infoMap = new HashMap<>(values.size() / 2);
		for (int i = 0; i < values.size(); i += 2) {
			Object val = values.get(i + 1);
			if (val instanceof byte[]) {
				val = new String((byte[]) val);
			}
			if (val != null) {
				infoMap.put(new String((byte[]) values.get(i)), val);
			}
		}
		return infoMap;
	}

	/**
	 * 删除 bloomFilter
	 * @param key The key of the filter
	 * @return 删除成功返回 true
	 */
	public boolean delete(String key) {
		return execute(key, CommandType.DEL, new BooleanOutput<>(codec)).orElse(false);
	}

}
