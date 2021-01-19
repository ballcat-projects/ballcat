package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.codegen.model.qo.TableInfoQO;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.common.core.domain.PageParam;
import com.hccake.ballcat.common.core.domain.PageResult;
import com.hccake.extend.mybatis.plus.toolkit.PageUtil;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 代码生成器
 *
 * @author Hccake
 */
@Mapper
public interface TableInfoMapper {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询数据
	 * @return PageResult<TableInfo>
	 */
	default PageResult<TableInfo> queryPage(PageParam pageParam, TableInfoQO qo) {
		IPage<TableInfo> page = PageUtil.prodPage(pageParam);
		this.selectByPage(page, qo.getTableName());
		return new PageResult<>(page.getRecords(), page.getTotal());
	}

	/**
	 * 分页查询表格
	 * @param page 分页参数
	 * @param tableName 表名
	 * @return 填充后的分页数据
	 */
	IPage<TableInfo> selectByPage(IPage<?> page, @Param("tableName") String tableName);

	/**
	 * 根据表名查询对应表信息
	 * @param tableName 表名
	 * @return TableInfo
	 */
	TableInfo queryTableInfo(@Param("tableName") String tableName);

	/**
	 * 查询列信息
	 * @param tableName 表名
	 * @return List<ColumnInfo>
	 */
	List<ColumnInfo> queryColumnInfo(@Param("tableName") String tableName);

}
