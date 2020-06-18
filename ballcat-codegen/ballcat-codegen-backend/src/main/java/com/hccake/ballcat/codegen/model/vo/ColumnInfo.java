package com.hccake.ballcat.codegen.model.vo;

import lombok.Data;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/17 19:45
 * 列信息
 */
@Data
public class ColumnInfo {

	/**
	 * 列表
	 */
	private String columnName;
	/**
	 * 数据类型
	 */
	private String dataType;
	/**
	 * 备注
	 */
	private String columnComment;
	/**
	 * 其他信息
	 */
	private String extra;
	/**
	 * 是否可以为空
	 */
	private String isNullable;
	/**
	 * 字段类型
	 */
	private String columnType;
	/**
	 * 索引类型
	 */
	private String columnKey;

}
