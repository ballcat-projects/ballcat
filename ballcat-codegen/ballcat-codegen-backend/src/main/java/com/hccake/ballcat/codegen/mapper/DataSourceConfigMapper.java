package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hccake.ballcat.codegen.converter.DataSourceConfigConverter;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.model.qo.DataSourceConfigQO;
import com.hccake.ballcat.codegen.model.vo.DataSourceConfigPageVO;
import com.hccake.ballcat.common.model.domain.PageParam;
import com.hccake.ballcat.common.model.domain.PageResult;
import com.hccake.ballcat.common.model.domain.SelectData;
import com.hccake.extend.mybatis.plus.conditions.query.LambdaQueryWrapperX;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;
import com.hccake.extend.mybatis.plus.toolkit.WrappersX;

import java.util.List;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
public interface DataSourceConfigMapper extends ExtendMapper<DataSourceConfig> {

	/**
	 * 分页查询
	 * @param pageParam 分页参数
	 * @param qo 查询条件
	 * @return PageResult<DataSourceConfigVO> 分页结果数据
	 */
	default PageResult<DataSourceConfigPageVO> queryPage(PageParam pageParam, DataSourceConfigQO qo) {
		IPage<DataSourceConfig> page = this.prodPage(pageParam);
		LambdaQueryWrapperX<DataSourceConfig> wrapperX = WrappersX.lambdaQueryX(DataSourceConfig.class)
				.eqIfPresent(DataSourceConfig::getId, qo.getId());
		this.selectPage(page, wrapperX);
		IPage<DataSourceConfigPageVO> voPage = page.convert(DataSourceConfigConverter.INSTANCE::poToPageVo);
		return new PageResult<>(voPage.getRecords(), voPage.getTotal());
	}

	/**
	 * 获取SelectData集合
	 * @return List<SelectData<?>>
	 */
	List<SelectData<?>> listSelectData();

}
