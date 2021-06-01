package com.hccake.ballcat.log.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 操作日志视图对象
 *
 * @author hccake
 * @date 2019-10-15 20:42:32
 */
@Data
@ApiModel(value = "操作日志视图对象")
public class OperationLogPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@ApiModelProperty(value = "编号")
	private Long id;

	/**
	 * 追踪ID
	 */
	@ApiModelProperty(value = "追踪ID")
	private String traceId;

	/**
	 * 日志消息
	 */
	@ApiModelProperty(value = "日志消息")
	private String msg;

	/**
	 * 访问IP地址
	 */
	@ApiModelProperty(value = "访问IP地址")
	private String ip;

	/**
	 * 用户代理
	 */
	@ApiModelProperty(value = "用户代理")
	private String userAgent;

	/**
	 * 请求URI
	 */
	@ApiModelProperty(value = "请求URI")
	private String uri;

	/**
	 * 请求方法
	 */
	@ApiModelProperty(value = "请求方法")
	private String method;

	/**
	 * 操作提交的数据
	 */
	@ApiModelProperty(value = "操作提交的数据")
	private String params;

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
	 * 执行时长
	 */
	@ApiModelProperty(value = "执行时长")
	private Long time;

	/**
	 * 创建者
	 */
	@ApiModelProperty(value = "创建者")
	private String operator;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

}
