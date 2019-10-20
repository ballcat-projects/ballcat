package com.hccake.ballcat.codegen.vo;

import com.hccake.ballcat.codegen.config.ReqGenConfig;
import lombok.Data;

/**
 * @author
 * 生成参数vo
 */
@Data
public class GeneratorVo {

	/**
	 * 配置信息
	 */
	private ReqGenConfig genConfig;
	/**
	 * 表名称
	 */
	private String[] tableNames;

}
