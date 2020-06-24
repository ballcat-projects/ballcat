package com.hccake.ballcat.common.redis.core;

import com.hccake.ballcat.common.redis.config.CachePropertiesHolder;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/3 9:58 缓存key的生成工具类，主要用于解析spel, 进行拼接key的生成
 */
public class KeyGenerator {

	/**
	 * SpEL 上下文
	 */
	StandardEvaluationContext spElContext;

	public KeyGenerator(Object target, Method method, Object[] arguments) {
		this.spElContext = SpELUtil.getSpElContext(target, method, arguments);
	}

	public String getKey(String key, String spELExpressions) {
		// 根据keyJoint 判断是否需要拼接
		if (spELExpressions == null || spELExpressions.length() == 0) {
			return key;
		}

		// 获取所有需要拼接的元素, 组装进集合中
		String joint = SpELUtil.parseValueToString(spElContext, spELExpressions);
		Assert.notNull(joint, "Key joint cannot be null!");

		if (StringUtils.isEmpty(key)) {
			return joint;
		}
		// 拼接后返回
		return jointKey(key, joint);
	}

	public List<String> getKeys(String key, String keyJoint, Collection<String> multiByItem) {
		String keyPrefix = getKey(key, keyJoint);

		List<String> list = new ArrayList<>();
		for (String item : multiByItem) {
			list.add(jointKey(keyPrefix, item));
		}

		return list;
	}

	/**
	 * @param key
	 * @param spELExpressions
	 * @return
	 */
	public String getKeys(String key, String[] spELExpressions) {
		// 根据keyJoint 判断是否需要拼接
		if (spELExpressions == null || spELExpressions.length == 0) {
			return key;
		}

		// 获取所有需要拼接的元素, 组装进集合中
		List<String> list = new ArrayList<>(spELExpressions.length + 1);
		list.add(key);
		for (String joint : spELExpressions) {
			String s = parseSpEL(joint);
			Assert.notNull(s, "Key joint cannot be null!");
			list.add(s);
		}

		// 拼接后返回
		return jointKey(list);
	}

	/**
	 * 解析SPEL
	 * @param field
	 * @return
	 */
	public String parseSpEL(String field) {
		return SpELUtil.parseValueToString(spElContext, field);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param list
	 * @return
	 */
	public String jointKey(List<String> list) {
		return String.join(CachePropertiesHolder.delimiter(), list);
	}

	/**
	 * 拼接key, 默认使用 ：作为分隔符
	 * @param items
	 * @return
	 */
	public String jointKey(String... items) {
		return jointKey(Arrays.asList(items));
	}

}
