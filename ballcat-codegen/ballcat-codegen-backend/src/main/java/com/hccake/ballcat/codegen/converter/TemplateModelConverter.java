package com.hccake.ballcat.codegen.converter;

import com.hccake.ballcat.codegen.model.dto.TemplateDirectoryCreateDTO;
import com.hccake.ballcat.codegen.model.dto.TemplateInfoDTO;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectory;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/19 20:01
 */
@Mapper
public interface TemplateModelConverter {

	TemplateModelConverter INSTANCE = Mappers.getMapper(TemplateModelConverter.class);

	/**
	 * 模板组 PO 转换为 PageVO
	 * @param templateGroup 模板组实体
	 * @return TemplateGroupPageVO 模板组分页VO
	 */
	TemplateGroupPageVO groupPoToPageVo(TemplateGroup templateGroup);

	/**
	 * 转换 DirectoryEntryPO 为 DirectoryEntryVO
	 * @param templateDirectoryEntry templateDirectoryEntry
	 * @return VO
	 */
	TemplateDirectoryEntryVO entryPoToVo(TemplateDirectoryEntry templateDirectoryEntry);

	/**
	 * 转换 DirectoryEntryCreateDTO to DirectoryEntry
	 * @param directoryCreateDTO directoryCreateDTO
	 * @return TemplateDirectoryEntry 持久对象
	 */
	TemplateDirectoryEntry entryCreateDtoToPo(TemplateDirectoryCreateDTO directoryCreateDTO);

	/**
	 * 转换为目录树
	 * @param templateDirectoryEntry templateDirectoryEntry
	 * @return TemplateDirectoryTree
	 */
	TemplateDirectory entryPoToTree(TemplateDirectoryEntry templateDirectoryEntry);

	/**
	 * TemplateInfoDto 转换为 TemplateInfo
	 * @param templateInfoDTO 模板详情信息DTO
	 * @return TemplateInfo
	 */
	TemplateInfo infoDtoToPo(TemplateInfoDTO templateInfoDTO);

}
