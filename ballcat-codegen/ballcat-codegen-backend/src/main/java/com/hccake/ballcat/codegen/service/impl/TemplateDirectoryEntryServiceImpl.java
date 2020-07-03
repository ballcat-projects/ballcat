package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.hccake.ballcat.codegen.constant.DirectoryEntryRemoveModeEnum;
import com.hccake.ballcat.codegen.constant.DirectoryEntryTypeEnum;
import com.hccake.ballcat.codegen.mapper.TemplateDirectoryEntryMapper;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.converter.TemplateModelConverter;
import com.hccake.ballcat.codegen.model.dto.TemplateDirectoryCreateDTO;
import com.hccake.ballcat.codegen.model.dto.TemplateInfoDTO;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectory;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectoryEntryVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.codegen.service.TemplateInfoService;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.exception.BusinessException;
import com.hccake.ballcat.common.core.result.BaseResultCode;
import com.hccake.ballcat.common.core.util.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 模板文件目录项
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Service
@RequiredArgsConstructor
public class TemplateDirectoryEntryServiceImpl extends ServiceImpl<TemplateDirectoryEntryMapper, TemplateDirectoryEntry>
		implements TemplateDirectoryEntryService {

	private final static String TABLE_ALIAS_PREFIX = "tde.";

	private final TemplateInfoService templateInfoService;

	/**
	 * 查询指定模板组下所有的目录项
	 * @param templateGroupId 模板组ID
	 * @return 所有的目录项
	 */
	@Override
	public List<TemplateDirectoryEntryVO> queryDirectoryEntry(Integer templateGroupId) {
		LambdaQueryWrapper<TemplateDirectoryEntry> wrapper = Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getGroupId, templateGroupId);
		List<TemplateDirectoryEntry> templateDirectoryEntries = baseMapper.selectList(wrapper);
		return templateDirectoryEntries.stream().map(TemplateModelConverter.INSTANCE::entryPoToVo)
				.collect(Collectors.toList());
	}

	/**
	 * 移动目录项
	 * @param horizontalMove 是否移动到目标目录平级，否则移动到其内部
	 * @param entryId 被移动的目录项ID
	 * @param targetEntryId 目标目录项ID
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
		Assert.isTrue(DirectoryEntryTypeEnum.FOLDER.getType().equals(targetEntry.getType()),
				"The target is not a folder");

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
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}

	/**
	 * 重名校验，同文件夹下不允许重名
	 * @param entryId 目录项ID
	 * @param name 文件名
	 */
	@Override
	public void duplicateNameCheck(Integer entryId, String name) {
		Integer count = baseMapper.selectCount(Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getParentId, entryId).eq(TemplateDirectoryEntry::getFileName, name));
		boolean notExist = count == null || count == 0;
		Assert.isTrue(notExist, "The entry with the same name already exists");
	}

	/**
	 * 判断目录项是否存在
	 * @param entryId 目录项ID
	 * @return boolean 存在：true
	 */
	@Override
	public boolean exists(Integer entryId) {
		Integer count = baseMapper
				.selectCount(Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getId, entryId));
		return count != null && count != 0;
	}

	/**
	 * 重命名目录项
	 * @param entryId 目录项ID
	 * @param name 名称
	 * @return boolean 成功：true
	 */
	@Override
	public boolean rename(Integer entryId, String name) {
		// 目录项必须存在
		TemplateDirectoryEntry entry = baseMapper.selectById(entryId);
		Assert.notNull(entry, "This is a nonexistent directory entry!");
		// 重名校验
		this.duplicateNameCheck(entry.getParentId(), name);
		// 更新
		TemplateDirectoryEntry entity = new TemplateDirectoryEntry();
		entity.setId(entryId);
		entity.setFileName(name);
		return SqlHelper.retBool(baseMapper.updateById(entity));
	}

	/**
	 * 删除目录项
	 * @param entryId 目录项id
	 * @param mode 删除模式
	 * @return boolean 成功：true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean removeEntry(Integer entryId, Integer mode) {
		TemplateDirectoryEntry entry = baseMapper.selectById(entryId);
		Assert.notNull(entry, "This is a nonexistent directory entry!");

		// 如果是文件夹类型，则根据删除模式进行子节点删除或上移操作
		if (DirectoryEntryTypeEnum.FOLDER.getType().equals(entry.getType())) {
			if (DirectoryEntryRemoveModeEnum.RESERVED_CHILD_NODE.getType().equals(mode)) {
				// 子节点上移
				baseMapper.update(null,
						Wrappers.<TemplateDirectoryEntry>lambdaUpdate()
								.set(TemplateDirectoryEntry::getParentId, entry.getParentId())
								.eq(TemplateDirectoryEntry::getParentId, entryId));
			}
			else if (DirectoryEntryRemoveModeEnum.REMOVE_CHILD_NODE.getType().equals(mode)) {
				// ==========删除所有子节点=============
				// 1. 获取所有目录项（目录项不会太多，一次查询比较方便）
				List<TemplateDirectoryEntry> entryList = baseMapper.selectList(Wrappers.emptyWrapper());
				// 2. 获取当前删除目录项的孩子节点列表
				List<TemplateDirectory> treeList = TreeUtil.buildTree(entryList, entryId,
						TemplateModelConverter.INSTANCE::entryPoToTree);
				// 3. 获取当前删除目录项的孩子节点Id
				List<Integer> treeNodeIds = TreeUtil.getTreeNodeIds(treeList);
				// 4. 删除所有孩子节点
				if (CollectionUtil.isNotEmpty(treeNodeIds)) {
					baseMapper.deleteBatchIds(treeNodeIds);
				}
				// 5. 删除模板文件信息(Id一样)
				templateInfoService.removeByIds(treeNodeIds);
			}
			else {
				throw new BusinessException(BaseResultCode.LOGIC_CHECK_ERROR.getCode(), "error delete mode");
			}
		}
		else {
			// 关联文件信息删除
			templateInfoService.removeById(entryId);
		}
		// 删除自身
		return SqlHelper.retBool(baseMapper.deleteById(entryId));
	}

	/**
	 * 复制模板目录项文件
	 * @param resourceId 原模板组
	 * @param groupId 模板模板组
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void copy(Integer resourceId, Integer groupId) {
		// 1. ===============获取模板目录项==================
		List<TemplateDirectoryEntry> list = baseMapper.selectList(
				Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getGroupId, resourceId));

		// 2. ============== 复制模板文件 ===================
		Set<Integer> oldParentIdSet = new HashSet<>();
		List<Integer> originEntryIds = new ArrayList<>();
		for (TemplateDirectoryEntry entry : list) {
			originEntryIds.add(entry.getId());
			oldParentIdSet.add(entry.getParentId());

			entry.setGroupId(groupId);
			entry.setId(null);
			entry.setCreateTime(null);
			entry.setUpdateTime(null);
		}
		this.saveBatch(list);

		// 3. =============== 获取新老ID的映射表，key: oldId, value: newId ==========
		Map<Integer, Integer> idMap = new HashMap<>();
		for (int i = 0; i < originEntryIds.size(); i++) {
			idMap.put(originEntryIds.get(i), list.get(i).getId());
		}

		// 4. =============== 更新复制出来的模板文件的父级ID ===============
		// 父节点为根节点的不需要修改
		oldParentIdSet.remove(GlobalConstants.TREE_ROOT_ID);
		for (Integer oldParentId : oldParentIdSet) {
			baseMapper.update(null,
					Wrappers.<TemplateDirectoryEntry>lambdaUpdate()
							.set(TemplateDirectoryEntry::getParentId, idMap.get(oldParentId))
							.eq(TemplateDirectoryEntry::getParentId, oldParentId)
							.eq(TemplateDirectoryEntry::getGroupId, groupId));
		}

		// 5. ================保存模板文件详情信息===================
		List<TemplateInfo> templateInfoList = templateInfoService
				.list(Wrappers.<TemplateInfo>lambdaQuery().eq(TemplateInfo::getGroupId, resourceId));
		for (TemplateInfo templateInfo : templateInfoList) {
			Integer oldId = templateInfo.getDirectoryEntryId();
			Integer newId = idMap.get(oldId);
			templateInfo.setDirectoryEntryId(newId);

			templateInfo.setCreateTime(null);
			templateInfo.setUpdateTime(null);
		}
		templateInfoService.saveBatch(templateInfoList);

	}

	/**
	 * 获取模板文件
	 * @param groupId 模板组Id
	 * @param templateFileIds 指定的文件id
	 * @return List 模板文件
	 */
	@Override
	public List<TemplateFile> findTemplateFiles(Integer groupId, Set<Integer> templateFileIds) {
		// 获取模板目录项
		List<TemplateDirectoryEntry> list = baseMapper.selectList(
				Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getGroupId, groupId));
		// 当没有指定时，不生成该文件
		if (CollectionUtil.isNotEmpty(templateFileIds)) {
			list.removeIf(entry -> DirectoryEntryTypeEnum.FILE.getType().equals(entry.getType())
					&& !templateFileIds.contains(entry.getId()));
		}

		// 转树形目录结构
		List<TemplateDirectory> treeList = TreeUtil.buildTree(list, GlobalConstants.TREE_ROOT_ID,
				TemplateModelConverter.INSTANCE::entryPoToTree);

		// 填充模板文件
		List<TemplateFile> templateFiles = new ArrayList<>();
		for (TemplateDirectory tree : treeList) {
			fillTemplateFiles(tree, templateFiles, "");
		}
		return templateFiles;
	}

	/**
	 * 填充模板文件信息
	 * @param current 当前目录项
	 * @param list 模板文件列表
	 * @param path 当前目录路径
	 */
	@SuppressWarnings("unchecked")
	private void fillTemplateFiles(TemplateDirectory current, List<TemplateFile> list, String path) {

		// 文件夹类型则递归子节点
		if (DirectoryEntryTypeEnum.FOLDER.getType().equals(current.getType())) {
			List<TemplateDirectory> children = (List<TemplateDirectory>) current.getChildren();
			// 递归调用子节点，查找叶子节点
			if (CollectionUtil.isNotEmpty(children)) {
				for (TemplateDirectory child : children) {
					fillTemplateFiles(child, list, path + current.getFileName() + File.separator);
				}
			}
		}

		// 目录项类型为文件则记录（文件必然是叶子节点）
		if (DirectoryEntryTypeEnum.FILE.getType().equals(current.getType())) {
			// 查找对应的模板文件详情信息
			TemplateInfo templateInfo = templateInfoService.getById(current.getId());
			TemplateFile templateFile = new TemplateFile().setFileName(current.getFileName()).setFilePath(path)
					.setContent(templateInfo.getContent()).setEngineType(templateInfo.getEngineType());
			list.add(templateFile);
		}
	}

	/**
	 * 新建一个目录项
	 * @param entryDTO 目录项新建传输对象
	 * @return boolean 成功：true
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public boolean createEntry(TemplateDirectoryCreateDTO entryDTO) {
		// 若父节点不是根，则校验父级节点是否有效
		Integer parentId = entryDTO.getParentId();
		Assert.isTrue(GlobalConstants.TREE_ROOT_ID.equals(parentId) || this.exists(parentId),
				"This is a nonexistent parent directory entry!");
		// 重名校验
		this.duplicateNameCheck(parentId, entryDTO.getFileName());
		// 转持久层对象
		TemplateDirectoryEntry entity = TemplateModelConverter.INSTANCE.entryCreateDtoToPo(entryDTO);
		// 落库
		baseMapper.insert(entity);
		// 如果是文件，需要同步存储info
		if (DirectoryEntryTypeEnum.FILE.getType().equals(entity.getType())) {
			TemplateInfoDTO templateInfoDTO = entryDTO.getTemplateInfoDTO();
			TemplateInfo templateInfo = TemplateModelConverter.INSTANCE.infoDtoToPo(templateInfoDTO);
			templateInfo.setDirectoryEntryId(entity.getId());
			templateInfoService.save(templateInfo);
		}
		return true;
	}

}