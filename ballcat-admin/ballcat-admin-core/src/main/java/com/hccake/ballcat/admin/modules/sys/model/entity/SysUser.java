package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 系统用户表
 *
 * @author ballcat code generator
 * @date 2019-09-12 20:39:31
 */
@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "系统用户表")
public class SysUser extends Model<SysUser> {
private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId
    @ApiModelProperty(value="主键id")
    private Integer userId;
    /**
     * 登录账号
     */
    @ApiModelProperty(value="登录账号")
    private String username;
    /**
     * 昵称
     */
    @ApiModelProperty(value="昵称")
    private String nickname;
    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;
    /**
     * md5密码盐
     */
    @ApiModelProperty(value="md5密码盐")
    private String salt;
    /**
     * 头像
     */
    @ApiModelProperty(value="头像")
    private String avatar;
    /**
     * 性别(0-默认未知,1-男,2-女)
     */
    @ApiModelProperty(value="性别(0-默认未知,1-男,2-女)")
    private Integer sex;
    /**
     * 电子邮件
     */
    @ApiModelProperty(value="电子邮件")
    private String email;
    /**
     * 电话
     */
    @ApiModelProperty(value="电话")
    private String phone;
    /**
     * 状态(1-正常,2-冻结)
     */
    @ApiModelProperty(value="状态(1-正常,2-冻结)")
    private Integer status;
    /**
     * 删除状态(0-正常,1-已删除)
     */
    @TableLogic
    @ApiModelProperty(value="删除状态(0-正常,1-已删除)")
    private Integer delFlag;
    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty(value="1:系统用户， 2：客户用户")
    private Integer type;
    }
