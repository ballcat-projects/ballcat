package com.hccake.ballcat.common.log.operation.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:59
 */
@Data
@Accessors(chain = true)
public class OperationLogDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 跟踪ID
	 */
	@ApiModelProperty(value = "跟踪ID")
	private String traceId;

	/**
	 * 操作类型
	 */
	@ApiModelProperty(value = "操作类型")
	private Integer type;

	/**
	 * 日志消息
	 */
	@ApiModelProperty(value = "日志消息")
	private String msg;

	/**
	 * 请求方式
	 */
	@ApiModelProperty(value = "请求方式")
	private String method;

	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求uri")
	private String uri;

	/**
	 * 操作IP地址
	 */
	@ApiModelProperty(value = "操作ip地址")
	private String ip;

	/**
	 * 用户代理
	 */
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 操作提交的数据
	 */
	@ApiModelProperty(value = "执行参数")
	private String params;

	/**
	 * 执行时间
	 */
	@ApiModelProperty(value = "方法执行时间")
	private Long time;

	/**
	 * 操作状态
	 */
	@ApiModelProperty(value = "操作状态")
	private Integer status;

	/**
	 * 操作人
	 */
	@ApiModelProperty(value = "操作人")
	private String operator;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

}
