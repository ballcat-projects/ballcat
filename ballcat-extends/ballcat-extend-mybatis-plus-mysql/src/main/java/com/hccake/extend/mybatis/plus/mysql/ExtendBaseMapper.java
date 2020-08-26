package com.hccake.extend.mybatis.plus.mysql;

import com.hccake.extend.mybatis.plus.config.StaticConfig;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertIgnoreByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateFieldByBatch;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 所有的 Mapper接口 都需要继承当前接口 如果想自己定义其他的全局方法， 您的全局 BaseMapper 需要继承当前接口
 *
 * @author lingting 2020/5/27 11:39
 */
public interface ExtendBaseMapper<T> extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T> {

	/**
	 * 批量插入数据 实现类 {@link InsertByBatch}
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	int insertByBatch(@Param("collection") Collection<T> list);

	/**
	 * 批处理 如果重复则忽略 实现类 {@link InsertIgnoreByBatch}.
	 * @param list 值列表
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	int insertIgnoreByBatch(@Param("collection") Collection<T> list);

	/**
	 * 批处理 如果重复则更新 实现类 {@link InsertOrUpdateByBatch}
	 * @param list 值列表
	 * @param ignore 是否忽略全局配置的忽略字段 {@link StaticConfig#UPDATE_IGNORE_FIELDS}
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	int insertOrUpdateByBatch(@Param("collection") Collection<T> list, @Param("ignore") boolean ignore);

	/**
	 * 批处理 如果重复则更新 直接调用本方法会 忽略全局配置的忽略字段 {@link StaticConfig#UPDATE_IGNORE_FIELDS}
	 * @param list 值列表
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	default int insertOrUpdateByBatch(@Param("collection") Collection<T> list) {
		return insertOrUpdateByBatch(list, true);
	}

	/**
	 * 自定义 如果重复 需要更新的 field 当传入的 columns.ignore 属性为 true时 会使用您传入的 字段值 去覆盖 不在 columns.list
	 * 中 字段 的值 实现类 {@link InsertOrUpdateFieldByBatch}
	 * @param list 值列表
	 * @param columns 字段
	 * @return int
	 * @author lingting 2020-05-27 15:48:20
	 */
	int insertOrUpdateFieldByBatch(@Param("collection") Collection<T> list, @Param("columns") Columns<T> columns);

}