package com.hccake.ballcat.codegen.vo;

import lombok.Data;

/**
 * @author
 * 生成参数vo
 */
@Data
public class GeneratorVO {

	/**
	 * 配置
	 */
	private GenConfigVO genConfigVO;
	/**
	 * 表名称
	 */
	private String[] tableNames;


}
