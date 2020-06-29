package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hccake.ballcat.codegen.constant.DirectoryEntryTypeEnum;
import com.hccake.ballcat.codegen.mapper.TemplateGroupMapper;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.converter.TemplateModelConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.entity.TemplateGroup;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.model.qo.TemplateGroupQO;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectory;
import com.hccake.ballcat.codegen.model.vo.TemplateGroupVO;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.codegen.service.TemplateGroupService;
import com.hccake.ballcat.codegen.service.TemplateInfoService;
import com.hccake.ballcat.common.core.constant.GlobalConstants;
import com.hccake.ballcat.common.core.util.TreeUtil;
import com.hccake.ballcat.common.core.vo.SelectData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 模板组
 *
 * @author hccake
 * @date 2020-06-19 19:11:41
 */
@Service
@RequiredArgsConstructor
public class TemplateGroupServiceImpl extends ServiceImpl<TemplateGroupMapper, TemplateGroup>
		implements TemplateGroupService {

	private final static String TABLE_ALIAS_PREFIX = "tg.";

	private final TemplateDirectoryEntryService templateDirectoryEntryService;

	private final TemplateInfoService templateInfoService;

	/**
	 * 根据QueryObject查询分页数据
	 * @param page 分页参数
	 * @param qo 查询参数对象
	 * @return 分页数据
	 */
	@Override
	public IPage<TemplateGroupVO> selectPageVo(IPage<?> page, TemplateGroupQO qo) {
		QueryWrapper<TemplateGroup> wrapper = Wrappers.<TemplateGroup>query().eq(ObjectUtil.isNotNull(qo.getId()),
				TABLE_ALIAS_PREFIX + "Id", qo.getId());
		return baseMapper.selectPageVo(page, wrapper);
	}

	/**
	 * 查找指定模板组下所有的模板文件
	 * @param groupId 模板组ID
	 * @return List<TemplateFile>
	 */
	@Override
	public List<TemplateFile> findTemplateFiles(Integer groupId) {
		// 获取模板目录项
		List<TemplateDirectoryEntry> list = templateDirectoryEntryService
				.list(Wrappers.<TemplateDirectoryEntry>lambdaQuery().eq(TemplateDirectoryEntry::getGroupId, groupId));
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
	 * 获取SelectData数据
	 * @return List<SelectData<?>>
	 */
	@Override
	public List<SelectData<?>> getSelectData() {
		return baseMapper.getSelectData();
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

}