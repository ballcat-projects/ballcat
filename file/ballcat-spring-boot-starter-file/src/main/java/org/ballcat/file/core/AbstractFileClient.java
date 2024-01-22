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

package org.ballcat.file.core;

/**
 * @author 疯狂的狮子Li 2022-04-24
 */
public abstract class AbstractFileClient implements FileClient {

	protected String slash = "/";

	protected String rootPath;

	/**
	 * 获取操作的根路径
	 */
	public String getRoot() {
		return this.rootPath;
	}

	/**
	 * 获取完整路径
	 * @param relativePath 文件相对 getRoot() 的路径
	 */
	public String getWholePath(String relativePath) {
		if (relativePath.startsWith(this.slash)) {
			return getRoot() + relativePath.substring(1);
		}
		return getRoot() + relativePath;
	}

}
