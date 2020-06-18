package com.hccake.ballcat.codegen.service;

import com.hccake.ballcat.codegen.config.GenConfig;

/**
 * @author hccake
 * @date 2018/7/29
 */
public interface GeneratorService {
	/**
	 * 生成代码
	 * @param tableNames 表名列表
	 * @param genConfig 代码生成的基本配置信息
	 * @return 已生成的代码数据
	 */
	byte[] generatorCode(String[] tableNames, GenConfig genConfig);
}
