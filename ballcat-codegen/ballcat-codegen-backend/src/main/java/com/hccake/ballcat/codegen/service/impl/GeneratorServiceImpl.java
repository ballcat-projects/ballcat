package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.dto.GeneratorOptionDTO;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.codegen.service.TemplateDirectoryEntryService;
import com.hccake.ballcat.codegen.util.GenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author Hccake
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final TableInfoService tableInfoService;

	private final TemplateDirectoryEntryService templateDirectoryEntryService;

	/**
	 * 生成代码
	 * @param generatorOptionDTO 代码生成的一些配置信息
	 * @return 已生成的代码数据
	 */
	@Override
	public byte[] generatorCode(GeneratorOptionDTO generatorOptionDTO) throws IOException {
		try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
				ZipOutputStream zip = new ZipOutputStream(outputStream)) {

			// 根据tableName 查询最新的表单配置
			List<TemplateFile> templateFiles = templateDirectoryEntryService.listTemplateFiles(
					generatorOptionDTO.getTemplateGroupId(), generatorOptionDTO.getTemplateFileIds());
			Assert.notEmpty(templateFiles, "模板组中模板文件为空！");

			for (String tableName : generatorOptionDTO.getTableNames()) {
				// 查询表信息
				TableInfo tableInfo = tableInfoService.queryTableInfo(tableName);
				// 查询列信息
				List<ColumnInfo> columnInfoList = tableInfoService.listColumnInfo(tableName);
				// 生成代码
				GenUtils.generatorCode(generatorOptionDTO.getTablePrefix(), generatorOptionDTO.getGenProperties(),
						tableInfo, columnInfoList, zip, templateFiles);
			}
			// 手动结束 zip，防止文件末端未被写入
			zip.finish();
			return outputStream.toByteArray();
		}
	}

	@Override
	public Map<String, String> previewCode(GeneratorOptionDTO preGenerateOptionDTO) {
		// 根据tableName 查询最新的表单配置
		List<TemplateFile> templateFiles = templateDirectoryEntryService.listTemplateFiles(
				preGenerateOptionDTO.getTemplateGroupId(), preGenerateOptionDTO.getTemplateFileIds());
		Assert.notEmpty(templateFiles, "模板组中模板文件为空！");
		String[] tableNames = preGenerateOptionDTO.getTableNames();
		Assert.isTrue(ArrayUtil.isNotEmpty(tableNames) && tableNames.length == 1, "预览仅支持单表");
		// 获取表名
		String tableName = tableNames[0];
		// 查询表信息
		TableInfo tableInfo = tableInfoService.queryTableInfo(tableName);
		// 查询列信息
		List<ColumnInfo> columnInfoList = tableInfoService.listColumnInfo(tableName);
		// 生成代码
		return GenUtils.previewCode(preGenerateOptionDTO.getTablePrefix(), preGenerateOptionDTO.getGenProperties(),
				tableInfo, columnInfoList, templateFiles);

	}

}
