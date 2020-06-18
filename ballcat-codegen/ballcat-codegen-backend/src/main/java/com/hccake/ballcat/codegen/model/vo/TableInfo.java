package com.hccake.ballcat.codegen.model.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/17 15:10
 */
@Data
public class TableInfo {

	/**
	 * 表名
	 */
	private String tableName;

	/**
	 * 存储引擎
	 */
	private String engine;

	/**
	 * 表备注
	 */
	private String tableComment;

	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;

}
