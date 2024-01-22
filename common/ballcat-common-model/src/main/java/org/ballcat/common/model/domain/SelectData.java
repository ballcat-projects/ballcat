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

package org.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

/**
 * 下拉框所对应的视图类
 *
 * @author Hccake
 */
@Data
@Schema(title = "下拉框数据")
public class SelectData<T> {

	/**
	 * 显示的数据
	 */
	@Schema(title = "显示的数据", requiredMode = REQUIRED)
	private String name;

	/**
	 * 选中获取的属性
	 */
	@Schema(title = "选中获取的属性", requiredMode = REQUIRED)
	private Object value;

	/**
	 * 是否被选中
	 */
	@Schema(title = "是否被选中")
	private Boolean selected;

	/**
	 * 是否禁用
	 */
	@Schema(title = "是否禁用")
	private Boolean disabled;

	/**
	 * 分组标识
	 */
	@Schema(title = "分组标识")
	private String type;

	/**
	 * 附加属性
	 */
	@Schema(title = "附加属性")
	private T attributes;

}
