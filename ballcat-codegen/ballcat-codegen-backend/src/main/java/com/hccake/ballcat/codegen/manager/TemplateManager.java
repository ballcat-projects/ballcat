package com.hccake.ballcat.codegen.manager;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.converter.TemplateDirectoryEntryConverter;
import com.hccake.ballcat.codegen.model.entity.TemplateDirectoryEntry;
import com.hccake.ballcat.codegen.model.entity.TemplateInfo;
import com.hccake.ballcat.codegen.model.vo.TemplateDirectory;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.codegen.service.TemplateInfoService;
import com.hccake.ballcat.common.core.util.TreeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 16:46
 * 模板管理
 */
@RequiredArgsConstructor
@Component
public class TemplateManager {
	private final TemplateDirectoryEntryService templateDirectoryEntryService;
	private final TemplateInfoService templateInfoService;

	/**
	 * 查找指定模板组下所有的模板文件
	 * @param groupId 模板组ID
	 * @return List<TemplateFile>
	 */
	public List<TemplateFile> findTemplateFiles(Integer groupId){
		// 获取模板目录项
		List<TemplateDirectoryEntry> list = templateDirectoryEntryService.list(Wrappers.<TemplateDirectoryEntry>lambdaQuery()
				.eq(TemplateDirectoryEntry::getGroupId, groupId));
		// 转树形目录结构
		List<TemplateDirectory> treeList =
				TreeUtil.buildTree(list, 0, TemplateDirectoryEntryConverter.INSTANCE::poToTree);

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
	public void fillTemplateFiles(TemplateDirectory current, List<TemplateFile> list, String path) {
		List<TemplateDirectory> children = (List<TemplateDirectory>) current.getChildren();
		// 为叶子节点且目录项类型为文件
		if (CollectionUtils.isEmpty(children) && current.getType() == 2) {
			// 查找对应的模板文件详情信息
			TemplateInfo templateInfo = templateInfoService.getById(current.getId());
			TemplateFile templateFile = new TemplateFile()
					.setFileName(current.getFileName())
					.setFilePath(path)
					.setContent(templateInfo.getContent())
					.setEngineType(templateInfo.getEngineType());
			list.add(templateFile);
			return;
		}
		// 递归调用子节点，查找叶子节点
		for (TemplateDirectory child : children) {
			fillTemplateFiles(child, list, path + current.getFileName() + File.separator);
		}
	}

}
