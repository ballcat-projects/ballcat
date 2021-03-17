package com.hccake.ballcat.codegen.mapper;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.extend.mybatis.plus.mapper.ExtendMapper;

import java.util.List;

/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
public interface TemplateDirectoryEntryMapper extends ExtendMapper<TemplateDirectoryEntry> {

	/**
	 * 根据模板组ID查询模板文件目录项集合
	 * @param templateGroupId 模板组ID
	 * @return List<TemplateDirectoryEntry>
	 */
	default List<TemplateDirectoryEntry> listByTemplateGroupId(Integer templateGroupId) {
		return this.selectList(
				Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getGroupId, templateGroupId));
	}

	/**
	 * 检测是否在指定目录下存在指定名称的文件
	 * @param entryId 目录项ID
	 * @param name 文件名称
	 * @return 是否存在
	 */
	default boolean existSameName(Integer entryId, String name) {
		Integer count = this.selectCount(Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getParentId, entryId).eq(TemplateDirectoryEntry::getFileName, name));
		return count != null && count > 0;
	}

	/**
	 * 判断目录项是否存在
	 * @param entryId 目录项ID
	 * @return boolean 存在：true
	 */
	default boolean existEntryId(Integer entryId) {
		Integer count = this
				.selectCount(Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getId, entryId));
		return count != null && count > 0;
	}

	/**
	 * 更新父级目录id
	 * @param groupId 分组ID
	 * @param oldParentId 老的父级ID
	 * @param newParentId 新增父级ID
	 */
	default void updateParentId(Integer groupId, Integer oldParentId, Integer newParentId) {
		LambdaUpdateWrapper<TemplateDirectoryEntry> wrapper = Wrappers.<TemplateDirectoryEntry>lambdaUpdate()
				.set(TemplateDirectoryEntry::getParentId, newParentId).eq(TemplateDirectoryEntry::getGroupId, groupId)
				.eq(TemplateDirectoryEntry::getParentId, oldParentId);
		this.update(null, wrapper);
	}

	/**
	 * 删除模板文件
	 * @param groupId 模板组ID
	 */
	default void deleteByGroupId(Integer groupId) {
		this.delete(Wrappers.lambdaQuery(TemplateDirectoryEntry.class).eq(TemplateDirectoryEntry::getGroupId, groupId));
	}

}