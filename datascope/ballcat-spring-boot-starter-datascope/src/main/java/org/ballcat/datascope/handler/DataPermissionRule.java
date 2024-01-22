/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.datascope.handler;

import org.ballcat.datascope.annotation.DataPermission;

/**
 * 数据权限的规则抽象类
 *
 * @author hccake
 * @since 0.7.0
 */
public class DataPermissionRule {

	private boolean ignore = false;

	private String[] includeResources = new String[0];

	private String[] excludeResources = new String[0];

	public DataPermissionRule() {
	}

	public DataPermissionRule(boolean ignore) {
		this.ignore = ignore;
	}

	public DataPermissionRule(boolean ignore, String[] includeResources, String[] excludeResources) {
		this.ignore = ignore;
		this.includeResources = includeResources;
		this.excludeResources = excludeResources;
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
		return this.ignore;
	}

	/**
	 * 仅对指定资源类型进行数据权限控制，只在开启情况下有效，当该数组有值时，exclude不生效
	 * @see DataPermission#excludeResources
	 * @return 资源类型数组
	 */
	public String[] includeResources() {
		return this.includeResources;
	}

	/**
	 * 对指定资源类型跳过数据权限控制，只在开启情况下有效，当该includeResources有值时，exclude不生效
	 * @see DataPermission#includeResources
	 * @return 资源类型数组
	 */
	public String[] excludeResources() {
		return this.excludeResources;
	}

	public DataPermissionRule setIgnore(boolean ignore) {
		this.ignore = ignore;
		return this;
	}

	public DataPermissionRule setIncludeResources(String[] includeResources) {
		this.includeResources = includeResources;
		return this;
	}

	public DataPermissionRule setExcludeResources(String[] excludeResources) {
		this.excludeResources = excludeResources;
		return this;
	}

}
