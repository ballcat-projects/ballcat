package com.hccake.ballcat.codegen.model.converter;

import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectory;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/19 20:01
 */
@Mapper
public interface TemplateDirectoryEntryConverter {
	TemplateDirectoryEntryConverter INSTANCE = Mappers.getMapper(TemplateDirectoryEntryConverter.class);

	/**
	 * 转换 PO 为 VO
	 * @param templateDirectoryEntry templateDirectoryEntry
	 * @return VO
	 */
	TemplateDirectoryEntryVO poToVo(TemplateDirectoryEntry templateDirectoryEntry);


	/**
	 * 转换为目录树
	 * @param templateDirectoryEntry templateDirectoryEntry
	 * @return TemplateDirectoryTree
	 */
	TemplateDirectory poToTree(TemplateDirectoryEntry templateDirectoryEntry);

}
