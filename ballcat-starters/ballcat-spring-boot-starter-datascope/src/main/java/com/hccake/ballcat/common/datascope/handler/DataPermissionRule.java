package com.hccake.ballcat.common.datascope.handler;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;

/**
 * @author hccake
 */
public class DataPermissionRule {

	private boolean ignore = false;

	private String[] includeResources = new String[0];

	private String[] excludeResources = new String[0];

	public DataPermissionRule() {
	}

	public DataPermissionRule(DataPermission dataPermission) {
		this.ignore = dataPermission.ignore();
		this.includeResources = dataPermission.includeResources();
		this.excludeResources = dataPermission.excludeResources();
	}

	/**
	 * 当前类或方法是否忽略数据权限
	 * @return boolean 默认返回 false
	 */
	public boolean ignore() {
		return ignore;
	}

	/**
	 * 仅对指定资源类型进行数据权限控制，只在开启情况下有效，当该数组有值时，exclude不生效
	 * @see DataPermission#excludeResources
	 * @return 资源类型数组
	 */
	public String[] includeResources() {
		return includeResources;
	}

	/**
	 * 对指定资源类型跳过数据权限控制，只在开启情况下有效，当该includeResources有值时，exclude不生效
	 * @see DataPermission#includeResources
	 * @return 资源类型数组
	 */
	public String[] excludeResources() {
		return excludeResources;
	}

	public void setIgnore(boolean ignore) {
		this.ignore = ignore;
	}

	public void setIncludeResources(String[] includeResources) {
		this.includeResources = includeResources;
	}

	public void setExcludeResources(String[] excludeResources) {
		this.excludeResources = excludeResources;
	}

}
