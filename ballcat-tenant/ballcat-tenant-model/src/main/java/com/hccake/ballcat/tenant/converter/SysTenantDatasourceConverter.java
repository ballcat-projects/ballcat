package com.hccake.ballcat.tenant.converter;

import com.hccake.ballcat.tenant.model.entity.SysTenantDatasource;
import com.hccake.ballcat.tenant.model.vo.SysTenantDatasourcePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户数据源映射表模型转换器
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Mapper
public interface SysTenantDatasourceConverter {

	SysTenantDatasourceConverter INSTANCE = Mappers.getMapper(SysTenantDatasourceConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysTenantDatasource 租户数据源映射表
	 * @return SysTenantDatasourcePageVO 租户数据源映射表PageVO
	 */
	SysTenantDatasourcePageVO poToPageVo(SysTenantDatasource sysTenantDatasource);

}
