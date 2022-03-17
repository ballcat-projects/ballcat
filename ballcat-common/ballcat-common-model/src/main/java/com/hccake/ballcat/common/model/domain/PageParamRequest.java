package com.hccake.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
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
	@Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", minimum = "1", defaultValue = "10", example = "10")
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	@Parameter(description = "排序字段，多个排序字段以,分割")
	@Schema(pattern = "^(" + PageParam.SORT_FILED_REGEX + ")(,(" + PageParam.SORT_FILED_REGEX + "))*$", example = "id")
	@Pattern(regexp = "^(" + PageParam.SORT_FILED_REGEX + ")(,(" + PageParam.SORT_FILED_REGEX + "))*$",
			message = "排序字段格式非法")
	String sortFields;

	@Parameter(description = "排序方式，与排序字段一一对应，以,分割")
	@Schema(pattern = "^(desc|asc)(,(desc|asc))*$", example = "desc")
	@Pattern(regexp = "^(desc|asc)(,(desc|asc))*$", message = "排序字段格式非法")
	String sortOrders;

}
