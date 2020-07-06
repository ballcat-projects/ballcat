package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.lang.Assert;
import com.hccake.ballcat.codegen.model.bo.TemplateFile;
import com.hccake.ballcat.codegen.model.dto.GeneratorOptionDTO;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.codegen.service.TemplateGroupService;
import com.hccake.ballcat.codegen.util.GenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author Hccake
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GeneratorServiceImpl implements GeneratorService {

	private final TableInfoService tableInfoService;

	private final TemplateGroupService templateGroupService;

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
			List<TemplateFile> templateFiles = templateGroupService.findTemplateFiles(
					generatorOptionDTO.getTemplateGroupId(), generatorOptionDTO.getTemplateFileIds());
			Assert.notEmpty(templateFiles, "模板组中模板文件为空！");

			for (String tableName : generatorOptionDTO.getTableNames()) {
				// 查询表信息
				TableInfo tableInfo = tableInfoService.queryTableInfo(tableName);
				// 查询列信息
				List<ColumnInfo> columnInfoList = tableInfoService.queryColumnInfo(tableName);
				// 生成代码
				GenUtils.generatorCode(generatorOptionDTO.getTablePrefix(), generatorOptionDTO.getGenProperties(),
						tableInfo, columnInfoList, zip, templateFiles);
			}
			return outputStream.toByteArray();
		}
	}

}
