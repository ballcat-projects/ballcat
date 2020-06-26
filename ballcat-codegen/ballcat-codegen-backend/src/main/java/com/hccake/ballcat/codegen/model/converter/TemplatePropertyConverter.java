package com.hccake.ballcat.codegen.model.converter;

import com.hccake.ballcat.codegen.model.entity.TemplateProperty;
import com.hccake.ballcat.codegen.model.vo.TemplatePropertyVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake 2020/6/26
 * @version 1.0
 */
@Mapper
public interface TemplatePropertyConverter {

	TemplatePropertyConverter INSTANCE = Mappers.getMapper(TemplatePropertyConverter.class);

	/**
	 * po转换为Vo
	 * @param property 模板属性
	 * @return 模板配置VO
	 */
	TemplatePropertyVO poToVo(TemplateProperty property);

}
