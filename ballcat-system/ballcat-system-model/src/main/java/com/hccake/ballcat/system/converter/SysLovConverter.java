package com.hccake.ballcat.system.converter;

import com.hccake.ballcat.system.model.entity.SysLov;
import com.hccake.ballcat.system.model.vo.LovInfoVO;
import com.hccake.ballcat.system.model.vo.SysLovPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake 2021/1/19
 * @version 1.0
 */
@Deprecated
@Mapper
public interface SysLovConverter {

	SysLovConverter INSTANCE = Mappers.getMapper(SysLovConverter.class);

	/**
	 * Lov 实体转换为 LovInfoVo 对象
	 * @param sysLov Lov实体
	 * @return LovVO
	 */
	@Mapping(target = "searchList", ignore = true)
	@Mapping(target = "bodyList", ignore = true)
	LovInfoVO poToInfoVO(SysLov sysLov);

	/**
	 * PO 转 PageVO
	 * @param sysLov lov
	 * @return LovPageVO lovPageVO
	 */
	SysLovPageVO poToPageVo(SysLov sysLov);

}
