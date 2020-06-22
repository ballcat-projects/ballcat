package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.codegen.mapper.TemplateDirectoryEntryMapper;
import com.hccake.ballcat.codegen.model.converter.TemplateDirectoryEntryConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Service
public class TemplateDirectoryEntryServiceImpl extends ServiceImpl<TemplateDirectoryEntryMapper, TemplateDirectoryEntry> implements TemplateDirectoryEntryService {
	private final static String TABLE_ALIAS_PREFIX = "tde.";

	/**
	 * 查询指定模板组下所有的目录项
	 *
	 * @param templateGroupId 模板组ID
	 * @return 所有的目录项
	 */
	@Override
	public List<TemplateDirectoryEntryVO> queryDirectoryEntry(Integer templateGroupId) {
		LambdaQueryWrapper<TemplateDirectoryEntry> wrapper = Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getGroupId, templateGroupId);
		List<TemplateDirectoryEntry> templateDirectoryEntries = baseMapper.selectList(wrapper);
		return templateDirectoryEntries.stream()
				.map(TemplateDirectoryEntryConverter.INSTANCE::poToVo)
				.collect(Collectors.toList());
	}

	/**
	 * 移动目录项
	 *
	 * @param horizontalMove 是否移动到目标目录平级，否则移动到其内部
	 * @param entryId        被移动的目录项ID
	 * @param targetEntryId  目标目录项ID
	 * @return boolean 移动成功或者失败
	 */
	@Override
	public boolean move(boolean horizontalMove, Integer entryId, Integer targetEntryId) {
		// 目录项必须存在
		TemplateDirectoryEntry entry = baseMapper.selectById(entryId);
		Assert.notNull(entry, "This is a nonexistent directory entry!");


		TemplateDirectoryEntry targetEntry = baseMapper.selectById(targetEntryId);
		// 目标必须存
		Assert.notNull(entry, "Target directory entry does not exist!");
		// 目标必须是文件夹
		Assert.isTrue(targetEntry.getType() == 1, "The target is not a folder");

		// 平级移动则目标父节点就是其父节点
		Integer parentId = horizontalMove ? targetEntry.getParentId() : targetEntry.getId();
		// 如果已经在该文件夹下则无需移动
		Assert.isFalse(parentId.equals(entry.getParentId()), "The entry do not need to be moved");

		// 重名校验
		this.duplicateNameCheck(parentId, entry.getFileName());

		// 更新目录项
		TemplateDirectoryEntry entity = new TemplateDirectoryEntry();
		entity.setId(entryId);
		entity.setParentId(parentId);
		boolean flag = SqlHelper.retBool(baseMapper.updateById(entity));
		return flag;
	}

	/**
	 * 重名校验，同文件夹下不允许重名
	 *
	 * @param entryId 目录项ID
	 * @param name    文件名
	 */
	private void duplicateNameCheck(Integer entryId, String name) {
		Integer count = baseMapper.selectCount(Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getParentId, entryId)
				.eq(TemplateDirectoryEntry::getFileName, name));
		boolean notExist = count == null || count == 0;
		Assert.isTrue(notExist, "The entry with the same name already exists");
	}

	/**
	 * 重命名目录项
	 *
	 * @param entryId 目录项ID
	 * @param name    名称
	 * @return boolean 成功：true
	 */
	@Override
	public boolean rename(Integer entryId, String name) {
		// 目录项必须存在
		TemplateDirectoryEntry entry = baseMapper.selectById(entryId);
		Assert.notNull(entry, "This is a nonexistent directory entry!");
		// 重名校验
		this.duplicateNameCheck(entry.getParentId(), entry.getFileName());
		// 更新
		TemplateDirectoryEntry entity = new TemplateDirectoryEntry();
		entity.setId(entryId);
		entity.setFileName(name);
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}


}