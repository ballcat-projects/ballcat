package com.hccake.ballcat.admin.modules.system.converter;

import com.hccake.ballcat.admin.modules.system.model.entity.Lov;
import com.hccake.ballcat.admin.modules.system.model.vo.LovInfoVO;
import com.hccake.ballcat.admin.modules.system.model.vo.LovPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake 2021/1/19
 * @version 1.0
 */
@Mapper
public interface LovConverter {

	LovConverter INSTANCE = Mappers.getMapper(LovConverter.class);

	/**
	 * Lov 实体转换为 LovInfoVo 对象
	 * @param lov Lov实体
	 * @return LovVO
	 */
	@Mapping(target = "searchList", ignore = true)
	@Mapping(target = "bodyList", ignore = true)
	LovInfoVO poToInfoVO(Lov lov);

	/**
	 * PO 转 PageVO
	 * @param lov lov
	 * @return LovPageVO lovPageVO
	 */
	LovPageVO poToPageVo(Lov lov);

}
