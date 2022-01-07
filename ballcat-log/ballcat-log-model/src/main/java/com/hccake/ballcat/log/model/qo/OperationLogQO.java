package com.hccake.ballcat.log.model.qo;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * 操作日志查询对象
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Data
@Schema(title = "操作日志查询对象")
@ParameterObject
public class OperationLogQO {

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
	 * 日志消息
	 */
	@Parameter(description = "日志消息")
	private String msg;

	/**
	 * 访问IP地址
	 */
	@Parameter(description = "访问IP地址")
	private String ip;

	/**
	 * 请求URI
	 */
	@Parameter(description = "请求URI")
	private String uri;

	/**
	 * 操作状态
	 */
	@Parameter(description = "操作状态")
	private Integer status;

	/**
	 * 操作类型
	 */
	@Parameter(description = "操作类型")
	private Integer type;

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
