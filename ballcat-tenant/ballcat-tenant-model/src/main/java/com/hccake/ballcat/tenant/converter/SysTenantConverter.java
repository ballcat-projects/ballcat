package com.hccake.ballcat.tenant.converter;

import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.model.vo.SysTenantPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 租户表模型转换器
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Mapper
public interface SysTenantConverter {

	SysTenantConverter INSTANCE = Mappers.getMapper(SysTenantConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysTenant 租户表
	 * @return SysTenantPageVO 租户表PageVO
	 */
	SysTenantPageVO poToPageVo(SysTenant sysTenant);

}
