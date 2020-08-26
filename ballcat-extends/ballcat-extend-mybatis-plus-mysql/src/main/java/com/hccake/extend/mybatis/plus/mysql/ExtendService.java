package com.hccake.extend.mybatis.plus.mysql;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hccake.extend.mybatis.plus.config.StaticConfig;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertIgnoreByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateByBatch;
import com.hccake.extend.mybatis.plus.mysql.methods.InsertOrUpdateFieldByBatch;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;

/**
 * 以前继承 com.baomidou.mybatisplus.extension.service.IService 的实现类，现在继承当前类
 *
 * @author lingting 2020/7/21 9:58
 */
public interface ExtendService<T> extends IService<T> {

	/**
	 * 批量插入数据
	 * @param list 数据列表
	 * @return int 改动行
	 * @author lingting 2020-08-26 22:11
	 */
	int insertByBatch(@Param("collection") Collection<T> list);

	/**
	 * 批处理 如果重复则忽略 实现类 {@link InsertIgnoreByBatch}
	 * @param list 值列表
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	int insertIgnoreByBatch(Collection<T> list);

	/**
	 * 批处理 如果重复则更新 实现类 {@link InsertOrUpdateByBatch}
	 * @param list 值列表
	 * @param ignore 是否忽略全局配置的忽略字段 {@link StaticConfig#UPDATE_IGNORE_FIELDS}
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	int insertOrUpdateByBatch(Collection<T> list, boolean ignore);

	/**
	 * 批处理 如果重复则更新 直接调用本方法会 忽略全局配置的忽略字段 {@link StaticConfig#UPDATE_IGNORE_FIELDS}
	 * @param list 值列表
	 * @return int
	 * @author lingting 2020-05-27 11:41:28
	 */
	default int insertOrUpdateByBatch(Collection<T> list) {
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
	int insertOrUpdateFieldByBatch(Collection<T> list, Columns<T> columns);

}
