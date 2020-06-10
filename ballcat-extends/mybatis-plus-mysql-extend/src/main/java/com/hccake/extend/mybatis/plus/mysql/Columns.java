package com.hccake.extend.mybatis.plus.mysql;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.LambdaUtils;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.core.toolkit.support.SerializedLambda;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.apache.ibatis.reflection.property.PropertyNamer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用于mybatis 自定义的 列
 *
 * @author lingting  2020/5/27 15:53
 */
@Getter
public class Columns<T> {
	public static final String COLUMN_FLAG = "@";
	/**
	 * 缓存               全类名         方法名   字段名
	 */
	private static final Map<String, ConcurrentHashMap<String, String>> COLUMN_CACHE_MAP = new ConcurrentHashMap<>();
	/**
	 * 自定义字段
	 */
	private final List<Column> list = new ArrayList<>();
	/**
	 * 类的所有字段
	 */
	private final List<String> back = new ArrayList<>();

	/**
	 * 是否忽略设置的字段
	 */
	private boolean ignore = false;

	private Columns() {
	}

	private Columns(SFunction<T, ?> sf) {
		this(sf, null);
	}

	private Columns(SFunction<T, ?> sf, String val) {
		this.add(sf, val);
	}

	public static <T> Columns<T> create() {
		return new Columns<>();
	}

	public static <T> Columns<T> create(SFunction<T, ?> sf) {
		return new Columns<>(sf);
	}

	public static <T> Columns<T> create(SFunction<T, ?> sf, String val) {
		return new Columns<>(sf, val);
	}

	public Columns<T> add(SFunction<T, ?> sf) {
		return add(sf, null);
	}

	/**
	 * @param val 自定义的替换sql  {@link Columns#COLUMN_FLAG} 表示字段名
	 * @author lingting  2020-05-27 17:57:35
	 */
	public Columns<T> add(SFunction<T, ?> sf, String val) {
		String column = getColumn(sf);
		if (StrUtil.isEmpty(val)) {
			val = "VALUES(" + column + ")";
		}

		list.add(new Column()
				.setName(column)
				.setVal(val.replaceAll(COLUMN_FLAG, column))
		);
		back.remove(column);
		return this;
	}

	/**
	 * 获取方法所代表的表的字段名
	 *
	 * @author lingting  2020-05-27 17:56:40
	 */
	public String getColumn(SFunction<T, ?> sf) {
		SerializedLambda lambda = SerializedLambda.resolve(sf);

		if (!COLUMN_CACHE_MAP.containsKey(lambda.getImplClass().getName())) {
			ConcurrentHashMap<String, String> map = new ConcurrentHashMap<>(LambdaUtils.getColumnMap(lambda.getImplClass()).size());
			LambdaUtils.getColumnMap(lambda.getImplClass()).forEach((k, v) -> {
				map.put(k, v.getColumn());
			});
			COLUMN_CACHE_MAP.put(lambda.getImplClass().getName(), map);
		}

		// 设置全字段名
		if (back.size() == 0 && COLUMN_CACHE_MAP.get(lambda.getImplClass().getName()).size() != 0) {
			COLUMN_CACHE_MAP.get(lambda.getImplClass().getName()).forEach((k, v) -> {
				back.add(v);
			});
		}

		return COLUMN_CACHE_MAP.get(lambda.getImplClass().getName())
				.get(LambdaUtils.formatKey(PropertyNamer.methodToProperty(lambda.getImplMethodName())));
	}

	/**
	 * 忽略设置的字段
	 *
	 * @author lingting  2020-05-28 11:06:15
	 */
	public Columns<T> ignore() {
		this.ignore = true;
		return this;
	}

	/**
	 * 设置不忽略设置的字段
	 *
	 * @author lingting  2020-05-28 11:05:59
	 */
	public Columns<T> set() {
		this.ignore = false;
		return this;
	}

	@Data
	@Accessors(chain = true)
	public static class Column {
		private String name;
		private String val;
	}
}

