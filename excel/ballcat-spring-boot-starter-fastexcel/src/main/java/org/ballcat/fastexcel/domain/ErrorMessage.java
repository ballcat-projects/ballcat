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

package org.ballcat.fastexcel.domain;

import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 校验错误信息
 *
 * @author lengleng 2021/8/4
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

	/**
	 * 行号
	 */
	private Long lineNum;

	/**
	 * 错误信息
	 */
	private Set<String> errors = new HashSet<>();

	public ErrorMessage(Set<String> errors) {
		this.errors = errors;
	}

	public ErrorMessage(String error) {
		HashSet<String> objects = new HashSet<>();
		objects.add(error);
		this.errors = objects;
	}

}
