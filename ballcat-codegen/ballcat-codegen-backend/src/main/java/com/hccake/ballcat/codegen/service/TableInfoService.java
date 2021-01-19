package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.qo.TableInfoQO;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;

import java.util.List;

/**
 * 表信息
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
public interface TableInfoService {

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	PageResult<TableInfo> queryPage(PageParam pageParam, TableInfoQO qo);

	/**
	 * 根据表名查询对应表信息
	 * @param tableName 表名
	 * @return TableInfo
	 */
	TableInfo queryTableInfo(String tableName);

	/**
	 * 查询指定表的列信息
	 * @param tableName 表名
	 * @return List<ColumnInfo>
	 */
	List<ColumnInfo> listColumnInfo(String tableName);

}
