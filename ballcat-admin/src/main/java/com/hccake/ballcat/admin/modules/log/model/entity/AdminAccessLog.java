package com.hccake.ballcat.admin.modules.log.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 后台访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Data
@TableName("admin_access_log")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "后台访问日志")
public class AdminAccessLog extends Model<AdminAccessLog> {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "编号")
    private Long id;
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
