package com.hccake.ballcat.common.datascope.handler;

import com.hccake.ballcat.common.datascope.DataScope;

import java.util.List;

/**
 * 数据权限处理器
 *
 * @author Hccake 2020/9/28
 * @version 1.0
 */
public interface DataPermissionHandler {

	/**
	 * 系统配置的所有的数据范围
	 * @return 数据范围集合
	 */
	List<DataScope> dataScopes();

	/**
	 * 是否忽略权限控制
	 * @return boolean true: 忽略，false: 进行权限控制
	 */
	boolean ignorePermissionControl();

}
