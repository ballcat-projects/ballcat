package com.hccake.ballcat.codegen.model.converter;

import com.hccake.ballcat.codegen.model.dto.DataSourceConfigDTO;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/18 9:49
 */
@Mapper
public interface DataSourceConfigConverter {

	DataSourceConfigConverter INSTANCE = Mappers.getMapper(DataSourceConfigConverter.class);

	/**
	 * 转换Dto为Po
	 * @param dataSourceConfigDTO DataSourceConfig
	 * @return dtoToPo
	 */
	DataSourceConfig dtoToPo(DataSourceConfigDTO dataSourceConfigDTO);
}
