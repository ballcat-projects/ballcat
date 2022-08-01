package com.hccake.ballcat.common.log.operation.enums;

/**
 * 操作类型的接口，用户可以继承此接口以便自定义类型
 *
 * @author hccake
 */
public final class OperationTypes {

	private OperationTypes() {
	}

	/**
	 * 其他操作
	 */
	public static final int OTHER = 0;

	/**
	 * 导入操作
	 */
	public static final int IMPORT = 1;

	/**
	 * 导出操作
	 */
	public static final int EXPORT = 2;

	/**
	 * 查看操作，主要用于敏感数据查询记录
	 */
	public static final int READ = 3;

	/**
	 * 新建操作
	 */
	public static final int CREATE = 4;

	/**
	 * 修改操作
	 */
	public static final int UPDATE = 5;

	/**
	 * 删除操作
	 */
	public static final int DELETE = 6;

}
