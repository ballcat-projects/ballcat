package com.hccake.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springdoc.api.annotations.ParameterObject;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

/**
 * 前端交互请求入参模型，将被转换为 PageParam 对象
 *
 * @see PageParam
 * @author hccake
 */
@Data
@Valid
@ParameterObject
@Schema(title = "分页查询入参")
public class PageParamRequest {

	@Parameter(description = "当前页码, 从 1 开始")
	@Schema(minimum = "1", defaultValue = "1", example = "1")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	@Parameter(description = "每页显示条数, 最大值为 100")
	@Schema(minimum = "1", maximum = "10", defaultValue = "10", example = "10")
	@Range(min = 1, max = 100, message = "条数范围为 [1, 100]")
	private long size = 10;

	@Parameter(description = "排序字段, 最大值为 100")
	@Schema(pattern = "[A-Za-z0-9_]{1,64}", example = "id")
	@Pattern(regexp = "[A-Za-z0-9_]{1,64}", message = "排序字段格式非法")
	String sortFields;

	@Parameter(description = "排序方式")
	@Schema(allowableValues = { "asc", "desc" }, example = "desc")
	String sortOrders;

}
