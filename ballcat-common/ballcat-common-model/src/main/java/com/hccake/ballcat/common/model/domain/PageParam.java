package com.hccake.ballcat.common.model.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

/**
 * 分页查询参数
 *
 * @author Hccake 2021/1/18
 * @version 1.0
 */
@Data
@Schema(title = "分页查询参数")
public class PageParam {

	@Schema(title = "当前页码", description = "从 1 开始", defaultValue = "1", example = "1")
	@Min(value = 1, message = "当前页不能小于 1")
	private long current = 1;

	@Schema(title = "每页显示条数", description = "最大值为系统设置，默认 100", defaultValue = "10")
	@Min(value = 1, message = "每页显示条数不能小于1")
	private long size = 10;

	@Schema(title = "排序规则")
	@Valid
	private List<Sort> sorts = new ArrayList<>();

	@Schema(title = "排序元素载体")
	@Getter
	@Setter
	public static class Sort {

		@Schema(title = "排序字段", example = "id")
		@Pattern(regexp = PageParamRequest.SORT_FILED_REGEX, message = "排序字段格式非法")
		private String field;

		@Schema(title = "是否正序排序", example = "false")
		private boolean asc;

	}

}
