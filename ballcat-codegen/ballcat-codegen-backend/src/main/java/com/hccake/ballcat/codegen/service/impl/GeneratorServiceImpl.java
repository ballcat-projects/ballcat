package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.hccake.ballcat.codegen.config.GenConfig;
import com.hccake.ballcat.codegen.model.vo.ColumnInfo;
import com.hccake.ballcat.codegen.model.vo.TableInfo;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.service.TableInfoService;
import com.hccake.ballcat.codegen.util.GenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.zip.ZipOutputStream;

/**
 * @author Hccake
 */
@Service
@RequiredArgsConstructor
@DS("#header.dsName")
public class GeneratorServiceImpl implements GeneratorService {
	private final TableInfoService tableInfoService;

	/**
	 * 生成代码
	 * @param tableNames 表名列表
	 * @param genConfig 代码生成的基本配置信息
	 * @return 已生成的代码数据
	 */
	@Override
	public byte[] generatorCode(String[] tableNames, GenConfig genConfig) {
		//根据tableName 查询最新的表单配置
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for (String tableName : tableNames) {
			//查询表信息
			TableInfo tableInfo = tableInfoService.queryTableInfo(tableName);
			//查询列信息
			List<ColumnInfo> columnInfoList = tableInfoService.queryColumnInfo(tableName);
			//生成代码
			GenUtils.generatorCode(genConfig, tableInfo, columnInfoList, zip);
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}
}
