package com.hccake.ballcat.admin.modules.system.converter;

import com.hccake.ballcat.admin.modules.system.model.entity.SysDictItem;
import com.hccake.ballcat.admin.modules.system.model.vo.DictItemVO;
import com.hccake.ballcat.admin.modules.system.model.vo.SysDictItemPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 字典项
 *
 * @author hccake 2021-03-22 19:55:41
 */
@Mapper
public interface SysDictItemConverter {

	SysDictItemConverter INSTANCE = Mappers.getMapper(SysDictItemConverter.class);

	/**
	 * PO 转 分页VO
	 * @param sysDictItem 字典项
	 * @return SysDictItemPageVO 字典项分页VO
	 */
	SysDictItemPageVO poToPageVo(SysDictItem sysDictItem);

	/**
	 * 字典项实体 转 VO
	 * @param sysDictItem 字典项
	 * @return 字典项VO
	 */
	DictItemVO poToItemVo(SysDictItem sysDictItem);

}
