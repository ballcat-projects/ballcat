package com.hccake.ballcat.commom.log.access.model;

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
public class AccessLogDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private String userId;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String username;
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
     * 请求参数
     */
    @ApiModelProperty(value = "请求参数")
    private String reqParams;
    /**
     * 请求体
     */
    @ApiModelProperty(value = "请求体")
    private String reqBody;
    /**
     * 请求时长
     */
    @ApiModelProperty(value = "请求时长")
    private Long time;
    /**
     * 响应状态码
     */
    @ApiModelProperty(value = "响应状态码")
    private Integer httpStatus;
    /**
     * 错误消息
     */
    @ApiModelProperty(value = "错误消息")
    private String errorMsg;
    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;

}
