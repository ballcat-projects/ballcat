package com.hccake.ballcat.codegen.model.bo;

import lombok.Data;

import java.util.List;

/**
 * 默认提供的模板渲染可用的属性
 * @author hccake
 */
@Data
public class GenerateProperties {

	/**
	 * 生成代码的即时时间
	 */
	private String currentTime;
	/**
	 * 表名
	 */
	private String tableName;
	/**
	 * 默认的表别名，主要用于多表联查使用
	 * 根据类名的大写字母拼接转小写得到
	 * eg: TableInfo => ti
	 */
	private String tableAlias;
	/**
	 * 类名：大驼峰
	 * eg: TableInfo
	 */
	private String className;
	/**
	 * 类名：小驼峰
	 * eg: tableInfo
	 */
	private String classname;
	/**
	 * 请求路径，全小写的类名
	 * eg: tableinfo
	 */
	private String pathName;
	/**
	 * 表备注
	 */
	private String comments;
	/**
	 * 主键
	 */
	private ColumnProperties pk;
	/**
	 * 列信息
	 */
	private List<ColumnProperties> columns;


	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
}
