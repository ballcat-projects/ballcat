package com.hccake.ballcat.admin.modules.log.model.qo;

import cn.hutool.core.date.DatePattern;
import com.hccake.ballcat.admin.constants.LoginEventTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 登陆日志 查询对象
 *
 * @author hccake 2020-09-16 20:21:10
 */
@Data
@ApiModel(value = "登陆日志查询对象")
public class AdminLoginLogQO {

	private static final long serialVersionUID = 1L;

	/**
	 * 追踪ID
	 */
	@ApiModelProperty(value = "追踪ID")
	private String traceId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 操作信息
	 */
	@ApiModelProperty(value = "操作信息")
	private String ip;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 * 事件类型 登陆/登出
	 * @see LoginEventTypeEnum
	 */
	@ApiModelProperty(value = "事件类型")
	private Integer eventType;

	/**
	 * 登陆时间区间（开始时间）
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@ApiModelProperty(value = "开始时间（登陆时间区间）")
	private LocalDateTime startTime;

	/**
	 * 登陆时间区间（结束时间）
	 */
	@DateTimeFormat(pattern = DatePattern.NORM_DATETIME_PATTERN)
	@ApiModelProperty(value = "结束时间（登陆时间区间）")
	private LocalDateTime endTime;

}