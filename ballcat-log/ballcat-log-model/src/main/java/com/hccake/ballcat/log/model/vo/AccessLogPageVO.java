package com.hccake.ballcat.log.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 访问日志展示对象
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Data
@ApiModel(value = "访问日志分页展示对象")
public class AccessLogPageVO {

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
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户ID")
	private Integer userId;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

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
	 * 请求映射地址
	 */
	@ApiModelProperty(value = "请求映射地址")
	private String matchingPattern;

	/**
	 * 操作方式
	 */
	@ApiModelProperty(value = "操作方式")
	private String method;

	/**
	 * 请求参数
	 */
	@ApiModelProperty(value = "请求参数")
	private String reqParams;

	/**
	 * 请求body
	 */
	@ApiModelProperty(value = "请求body")
	private String reqBody;

	/**
	 * 响应状态码
	 */
	@ApiModelProperty(value = "响应状态码")
	private Integer httpStatus;

	/**
	 * 响应信息
	 */
	@ApiModelProperty(value = "响应信息")
	private String result;

	/**
	 * 错误消息
	 */
	@ApiModelProperty(value = "错误消息")
	private String errorMsg;

	/**
	 * 执行时长
	 */
	@ApiModelProperty(value = "执行时长")
	private Long time;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

}
