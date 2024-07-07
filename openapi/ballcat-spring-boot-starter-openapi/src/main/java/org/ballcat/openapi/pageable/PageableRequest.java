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

package org.ballcat.openapi.pageable;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.ballcat.common.model.domain.PageParam;
import org.ballcat.common.model.domain.PageableConstants;
import org.springdoc.api.annotations.ParameterObject;

/**
 * 前端交互请求入参模型，将被转换为 PageParam 对象
 *
 * @see PageParam
 * @author hccake
 */
@Valid
@ParameterObject
@Getter
@Setter
@Schema(title = "分页查询入参")
public class PageableRequest {

	@Parameter(name = "page", description = "当前页码, 从 1 开始",
			schema = @Schema(minimum = "1", defaultValue = "1", example = "1"))
	@Min(value = 1, message = "当前页不能小于 1")
	private long page = 1;

	@Parameter(name = "size", description = "每页显示条数, 最大值为 100",
			schema = @Schema(title = "每页显示条数", description = "最大值视配置而定", minimum = "1", defaultValue = "10",
					example = "10"))
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size;

	@Parameter(name = "sort", description = "排序规则，格式为：property(,asc|desc)。" + "默认为升序。" + "支持传入多个排序字段。",
			array = @ArraySchema(
					schema = @Schema(type = "string", pattern = PageableConstants.SORT_REGEX, example = "id,desc")))
	private List<String> sort;

}
