package com.hccake.ballcat.log.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Data
@Schema(title = "后台访问日志查询对象")
@ParameterObject
public class AccessLogQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 追踪ID
	 */
	@Parameter(description = "追踪ID")
	private String traceId;

	/**
	 * 用户ID
	 */
	@Parameter(description = "用户ID")
	private Integer userId;

	/**
	 * 访问IP地址
	 */
	@Parameter(description = "访问IP地址")
	private String ip;

	/**
	 * 请求Uri
	 */
	@Parameter(description = "请求Uri")
	private String uri;

	/**
	 * 请求映射地址
	 */
	@Parameter(description = "请求映射地址")
	private String matchingPattern;

	/**
	 * 响应状态码
	 */
	@Parameter(description = "响应状态码")
	private Integer httpStatus;

	/**
	 * 登陆时间区间（开始时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Parameter(description = "开始时间（登陆时间区间）")
	private LocalDateTime startTime;

	/**
	 * 登陆时间区间（结束时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Parameter(description = "结束时间（登陆时间区间）")
	private LocalDateTime endTime;

}
