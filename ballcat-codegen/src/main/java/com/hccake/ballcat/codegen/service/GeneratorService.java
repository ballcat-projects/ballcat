package com.hccake.ballcat.codegen.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hccake.ballcat.codegen.config.GenConfig;

import java.util.List;
import java.util.Map;

/**
 * @author hccake
 * @date 2018/7/29
 */
public interface GeneratorService {
	/**
	 * 生成代码
	 * @param tableNames 表名列表
	 * @param genConfig 代码生成的基本配置信息
	 * @return
	 */
	byte[] generatorCode(String[] tableNames, GenConfig genConfig);

	/**
	 * 分页查询表
	 *
	 * @param page      分页信息
	 * @param tableName 表名
	 * @param id        数据源ID
	 * @return
	 */
	IPage<List<Map<String, Object>>> getPage(Page page, String tableName);
}
