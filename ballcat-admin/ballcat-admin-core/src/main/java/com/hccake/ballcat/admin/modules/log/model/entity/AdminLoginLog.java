package com.hccake.ballcat.admin.modules.log.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.admin.constants.LoginEventTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * 登陆日志
 *
 * @author hccake 2020-09-16 20:21:10
 */
@Data
@Accessors(chain = true)
@TableName("admin_login_log")
@ApiModel(value = "登陆日志")
public class AdminLoginLog {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@TableId
	@ApiModelProperty(value = "编号")
	private Long id;

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
	@ApiModelProperty(value = "登陆IP")
	private String ip;

	/**
	 * 操作系统
	 */
	@ApiModelProperty(value = "操作系统")
	private String os;

	/**
	 * 状态
	 */
	@ApiModelProperty(value = "状态")
	private Integer status;

	/**
	 * 日志消息
	 */
	@ApiModelProperty(value = "日志消息")
	private String msg;

	/**
	 * 登陆地点 TODO IP解析工具暂时未定 IP解析工具类需要简单封装下，方便替换底层工具
	 */
	@ApiModelProperty(value = "登陆地点")
	private String location;

	/**
	 * 事件类型 登陆/登出
	 * @see LoginEventTypeEnum
	 */
	@ApiModelProperty(value = "事件类型")
	private Integer eventType;

	/**
	 * 浏览器
	 */
	@ApiModelProperty(value = "浏览器")
	private String browser;

	/**
	 * 登录/登出时间
	 */
	@ApiModelProperty(value = "登录/登出时间")
	private LocalDateTime loginTime;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

}
