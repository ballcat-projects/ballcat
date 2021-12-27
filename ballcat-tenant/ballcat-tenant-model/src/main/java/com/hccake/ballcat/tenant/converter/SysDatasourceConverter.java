package com.hccake.ballcat.tenant.converter;

import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.vo.SysDatasourcePageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 数据源表模型转换器
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Mapper
public interface SysDatasourceConverter {

	SysDatasourceConverter INSTANCE = Mappers.getMapper(SysDatasourceConverter.class);

	/**
	 * PO 转 PageVO
	 * @param sysDatasource 数据源表
	 * @return SysDatasourcePageVO 数据源表PageVO
	 */
	SysDatasourcePageVO poToPageVo(SysDatasource sysDatasource);

}
