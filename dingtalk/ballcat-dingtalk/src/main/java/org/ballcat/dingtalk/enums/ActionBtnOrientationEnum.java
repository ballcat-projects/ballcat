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

package org.ballcat.dingtalk.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 跳转 ActionCard 类型 消息的按钮排列方式
 *
 * @author lingting 2020/6/10 23:44
 */
@Getter
@AllArgsConstructor
public enum ActionBtnOrientationEnum {

	/**
	 * 按钮排列样式值 说明
	 */
	VERTICAL("0", "按钮竖向排列"), HORIZONTAL("1", "按钮横向排列"),;

	private final String val;

	private final String text;

}
