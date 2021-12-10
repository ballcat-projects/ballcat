package com.hccake.ballcat.log.model.qo;

import com.hccake.ballcat.log.enums.LoginEventTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.hutool.core.date.DatePattern.NORM_DATETIME_PATTERN;

/**
 * 登陆日志 查询对象
 *
 * @author hccake 2020-09-16 20:21:10
 */
@Data
@Schema(title = "登陆日志查询对象")
@ParameterObject
public class LoginLogQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 追踪ID
	 */
	@Schema(title = "追踪ID")
	private String traceId;

	/**
	 * 用户名
	 */
	@Schema(title = "用户名")
	private String username;

	/**
	 * 操作信息
	 */
	@Schema(title = "操作信息")
	private String ip;

	/**
	 * 状态
	 */
	@Schema(title = "状态")
	private Integer status;

	/**
	 * 事件类型 登陆/登出
	 *
	 * @see LoginEventTypeEnum
	 */
	@Schema(title = "事件类型")
	private Integer eventType;

	/**
	 * 登陆时间区间（开始时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Schema(title = "开始时间（登陆时间区间）")
	private LocalDateTime startTime;

	/**
	 * 登陆时间区间（结束时间）
	 */
	@DateTimeFormat(pattern = NORM_DATETIME_PATTERN)
	@Schema(title = "结束时间（登陆时间区间）")
	private LocalDateTime endTime;

}