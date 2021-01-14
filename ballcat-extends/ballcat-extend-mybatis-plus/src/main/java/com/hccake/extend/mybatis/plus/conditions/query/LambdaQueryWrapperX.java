package com.hccake.extend.mybatis.plus.conditions.query;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.AbstractLambdaWrapper;
import com.baomidou.mybatisplus.core.conditions.SharedString;
import com.baomidou.mybatisplus.core.conditions.query.Query;
import com.baomidou.mybatisplus.core.conditions.segments.MergeSegments;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import com.baomidou.mybatisplus.core.toolkit.ArrayUtils;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 增加了一些简单条件的 IfPresent 条件 支持，Collection String Object 等等判断是否为空，或者是否为null
 *
 * @author Hccake 2021/1/14
 * @version 1.0
 */
public class LambdaQueryWrapperX<T> extends AbstractLambdaWrapper<T, LambdaQueryWrapperX<T>>
		implements Query<LambdaQueryWrapperX<T>, T, SFunction<T, ?>> {

	/**
	 * 查询字段
	 */
	private SharedString sqlSelect = new SharedString();

	/**
	 * 不建议直接 new 该实例，使用 WrappersX.lambdaQueryX(entity)
	 */
	public LambdaQueryWrapperX() {
		this((T) null);
	}

	/**
	 * 不建议直接 new 该实例，使用 WrappersX.lambdaQueryX(entity)
	 */
	public LambdaQueryWrapperX(T entity) {
		super.setEntity(entity);
		super.initNeed();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(entity)
	 */
	public LambdaQueryWrapperX(Class<T> entityClass) {
		super.setEntityClass(entityClass);
		super.initNeed();
	}

	/**
	 * 不建议直接 new 该实例，使用 Wrappers.lambdaQuery(...)
	 */
	LambdaQueryWrapperX(T entity, Class<T> entityClass, SharedString sqlSelect, AtomicInteger paramNameSeq,
			Map<String, Object> paramNameValuePairs, MergeSegments mergeSegments, SharedString lastSql,
			SharedString sqlComment, SharedString sqlFirst) {
		super.setEntity(entity);
		super.setEntityClass(entityClass);
		this.paramNameSeq = paramNameSeq;
		this.paramNameValuePairs = paramNameValuePairs;
		this.expression = mergeSegments;
		this.sqlSelect = sqlSelect;
		this.lastSql = lastSql;
		this.sqlComment = sqlComment;
		this.sqlFirst = sqlFirst;
	}

	/**
	 * SELECT 部分 SQL 设置
	 * @param columns 查询字段
	 */
	@SafeVarargs
	@Override
	public final LambdaQueryWrapperX<T> select(SFunction<T, ?>... columns) {
		if (ArrayUtils.isNotEmpty(columns)) {
			this.sqlSelect.setStringValue(columnsToString(false, columns));
		}
		return typedThis;
	}

	/**
	 * 过滤查询的字段信息(主键除外!)
	 * <p>
	 * 例1: 只要 java 字段名以 "test" 开头的 -> select(i -&gt; i.getProperty().startsWith("test"))
	 * </p>
	 * <p>
	 * 例2: 只要 java 字段属性是 CharSequence 类型的 -> select(TableFieldInfo::isCharSequence)
	 * </p>
	 * <p>
	 * 例3: 只要 java 字段没有填充策略的 -> select(i -&gt; i.getFieldFill() == FieldFill.DEFAULT)
	 * </p>
	 * <p>
	 * 例4: 要全部字段 -> select(i -&gt; true)
	 * </p>
	 * <p>
	 * 例5: 只要主键字段 -> select(i -&gt; false)
	 * </p>
	 * @param predicate 过滤方式
	 * @return this
	 */
	@Override
	public LambdaQueryWrapperX<T> select(Class<T> entityClass, Predicate<TableFieldInfo> predicate) {
		if (entityClass == null) {
			entityClass = getEntityClass();
		}
		else {
			setEntityClass(entityClass);
		}
		Assert.notNull(entityClass, "entityClass can not be null");
		this.sqlSelect.setStringValue(TableInfoHelper.getTableInfo(entityClass).chooseSelect(predicate));
		return typedThis;
	}

	@Override
	public String getSqlSelect() {
		return sqlSelect.getStringValue();
	}

	/**
	 * 用于生成嵌套 sql
	 * <p>
	 * 故 sqlSelect 不向下传递
	 * </p>
	 */
	@Override
	protected LambdaQueryWrapperX<T> instance() {
		return new LambdaQueryWrapperX<>(getEntity(), getEntityClass(), null, paramNameSeq, paramNameValuePairs,
				new MergeSegments(), SharedString.emptyString(), SharedString.emptyString(),
				SharedString.emptyString());
	}

	@Override
	public void clear() {
		super.clear();
		sqlSelect.toNull();
	}

	// ======= 分界线，以上 copy 自 mybatis-plus 源码 =====

	private boolean conditional(Object val) {
		return ObjectUtil.isNotEmpty(val);
	}

	public LambdaQueryWrapperX<T> eqIfPresent(SFunction<T, ?> column, Object val) {
		return super.eq(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> neIfPresent(SFunction<T, ?> column, Object val) {
		return super.ne(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> gtIfPresent(SFunction<T, ?> column, Object val) {
		return super.gt(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> geIfPresent(SFunction<T, ?> column, Object val) {
		return super.ge(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> ltIfPresent(SFunction<T, ?> column, Object val) {
		return super.lt(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> leIfPresent(SFunction<T, ?> column, Object val) {
		return super.le(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> likeIfPresent(SFunction<T, ?> column, Object val) {
		return super.like(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> notLikeIfPresent(SFunction<T, ?> column, Object val) {
		return super.notLike(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> likeLeftIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeLeft(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> likeRightIfPresent(SFunction<T, ?> column, Object val) {
		return super.likeRight(conditional(val), column, val);
	}

	public LambdaQueryWrapperX<T> inIfPresent(SFunction<T, ?> column, Collection<?> coll) {
		return super.in(conditional(coll), column, coll);
	}

	public LambdaQueryWrapperX<T> notInIfPresent(SFunction<T, ?> column, Collection<?> coll) {
		return super.notIn(conditional(coll), column, coll);
	}

}
