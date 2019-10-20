package com.hccake.ballcat.codegen.service.impl;

import cn.hutool.core.io.IoUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.config.GenConfig;
import com.hccake.ballcat.codegen.mapper.GeneratorMapper;
import com.hccake.ballcat.codegen.service.GeneratorService;
import com.hccake.ballcat.codegen.util.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author Hccake
 */
@Service
public class GeneratorServiceImpl implements GeneratorService {

	@Autowired
	private GeneratorMapper generatorMapper;

	/**
	 * 生成代码
	 * @param tableNames 表名列表
	 * @param genConfig 代码生成的基本配置信息
	 * @return
	 */
	@Override
	public byte[] generatorCode(String[] tableNames, GenConfig genConfig) {
		//根据tableName 查询最新的表单配置
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);

		for (String tableName : tableNames) {
			//查询表信息
			Map<String, String> table = queryTable(tableName);
			//查询列信息
			List<Map<String, String>> columns = queryColumns(tableName);
			//生成代码
			GenUtils.generatorCode(genConfig, table, columns, zip);
		}
		IoUtil.close(zip);
		return outputStream.toByteArray();
	}

	/**
	 * 分页查询表
	 *
	 * @param tableName 查询条件
	 * @return
	 */
	@Override
	public IPage<List<Map<String, Object>>> getPage(Page page, String tableName) {
		return generatorMapper.queryList(page, tableName);
	}

	private Map<String, String> queryTable(String tableName) {
		return generatorMapper.queryTable(tableName);
	}

	private List<Map<String, String>> queryColumns(String tableName) {
		return generatorMapper.queryColumns(tableName);
	}
}
