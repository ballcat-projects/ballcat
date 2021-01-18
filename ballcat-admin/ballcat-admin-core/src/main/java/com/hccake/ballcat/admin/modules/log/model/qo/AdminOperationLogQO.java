package com.hccake.ballcat.admin.modules.log.model.qo;

import cn.hutool.core.date.DatePattern;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 操作日志查询对象
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Data
@ApiModel(value = "操作日志查询对象")
public class AdminOperationLogQO {

	/**
	 * 追踪ID
	 */
	@ApiModelProperty(value = "追踪ID")
	private String traceId;

	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	/**
	 * 访问IP地址
	 */
	@ApiModelProperty(value = "访问IP地址")
	private String ip;

	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求URI")
	private String uri;

	/**
	 * 操作状态
	 */
	@ApiModelProperty(value = "操作状态")
	private Integer status;

	/**
	 * 操作类型
	 */
	@ApiModelProperty(value = "操作类型")
	private Integer type;

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
