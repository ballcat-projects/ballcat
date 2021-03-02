package com.hccake.ballcat.codegen.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.hccake.ballcat.codegen.mapper.TableInfoMapper;
import com.hccake.ballcat.codegen.model.qo.TableInfoQO;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 表信息
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
@Service
@RequiredArgsConstructor
@DS("#header.dsName")
public class TableInfoServiceImpl implements TableInfoService {

	private final TableInfoMapper baseMapper;

	/**
	 * 根据QueryObject查询分页数据
	 * @param pageParam 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public PageResult<TableInfo> queryPage(PageParam pageParam, TableInfoQO qo) {
		return baseMapper.queryPage(pageParam, qo);
	}

	/**
	 * 根据表名查询对应表信息
	 * @param tableName 表名
	 * @return TableInfo
	 */
	@Override
	public TableInfo queryTableInfo(String tableName) {
		return baseMapper.queryTableInfo(tableName);
	}

	/**
	 * 查询指定表的列信息
	 * @param tableName 表名
	 * @return List<ColumnInfo>
	 */
	@Override
	public List<ColumnInfo> listColumnInfo(String tableName) {
		return baseMapper.listColumnInfo(tableName);
	}

}
