package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.dto.TemplateDirectoryCreateDTO;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.extend.mybatis.plus.service.ExtendService;

import java.util.List;
import java.util.Set;

/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateDirectoryEntryService extends ExtendService<TemplateDirectoryEntry> {

	/**
	 * 查询指定模板组下所有的目录项
	 * @param templateGroupId 模板组ID
	 * @return 所有的目录项
	 */
	List<TemplateDirectoryEntry> listByTemplateGroupId(Integer templateGroupId);

	/**
	 * 移动目录项
	 * @param horizontalMove 是否移动到目标目录平级，否则移动到其内部
	 * @param entryId 被移动的目录项ID
	 * @param targetEntryId 目标目录项ID
	 * @return boolean
	 */
	boolean move(boolean horizontalMove, Integer entryId, Integer targetEntryId);

	/**
	 * 重名校验，同文件夹下不允许重名
	 * @param entryId 目录项ID
	 * @param name 文件名
	 */
	void duplicateNameCheck(Integer entryId, String name);

	/**
	 * 判断目录项是否存在
	 * @param entryId 目录项ID
	 * @return boolean 存在：true
	 */
	boolean exists(Integer entryId);

	/**
	 * 重命名目录项
	 * @param entryId 目录项ID
	 * @param name 名称
	 * @return boolean 成功：true
	 */
	boolean rename(Integer entryId, String name);

	/**
	 * 新建一个目录项
	 * @param templateDirectoryCreateDTO 目录项新建传输对象
	 * @return boolean 成功：true
	 */
	boolean createEntry(TemplateDirectoryCreateDTO templateDirectoryCreateDTO);

	/**
	 * 删除目录项
	 * @param entryId 目录项id
	 * @param mode 删除模式
	 * @return boolean 成功：true
	 */
	boolean removeEntry(Integer entryId, Integer mode);

	/**
	 * 获取模板文件
	 * @param groupId 模板组Id
	 * @param templateFileIds 模板文件ID集合
	 * @return List 模板文件
	 */
	List<TemplateFile> listTemplateFiles(Integer groupId, Set<Integer> templateFileIds);

	/**
	 * 复制模板目录项文件
	 * @param resourceGroupId 原模板组
	 * @param targetGroupId 模板模板组
	 */
	void copy(Integer resourceGroupId, Integer targetGroupId);

	/**
	 * 删除模板文件
	 * @param groupId 模板组ID
	 */
	void removeByGroupId(Integer groupId);
}