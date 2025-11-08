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

package org.ballcat.fieldcrypt.mybatis.metadata;

import lombok.Data;

/**
 * 方法返回值解密元数据.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
public class MybatisResultMetadata {

	public static final MybatisResultMetadata ABSENT = new MybatisResultMetadata(false, "", "");

	/**
	 * 是否被 #{@link org.ballcat.fieldcrypt.annotation.DecryptResult} 注解。
	 */
	private final boolean annotated;

	private final String algo;

	private final String params;

	public MybatisResultMetadata(boolean annotated, String algo, String params) {
		this.annotated = annotated;
		this.algo = algo == null ? "" : algo;
		this.params = params == null ? "" : params;
	}

}
