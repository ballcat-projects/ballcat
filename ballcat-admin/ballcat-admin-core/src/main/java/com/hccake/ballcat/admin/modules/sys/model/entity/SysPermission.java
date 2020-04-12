package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜单权限表
 * </p>
 *
 * @author
 * @since 2017-11-08
 */
@Data
@ApiModel(value = "菜单")
@TableName("sys_permission")
@EqualsAndHashCode(callSuper = true)
public class SysPermission extends Model<SysPermission> {

	private static final long serialVersionUID = 1L;
	/**
	 * 菜单ID
	 */
	@TableId
	@ApiModelProperty(value="菜单ID")
	private Integer id;
	/**
	 * 菜单标题
	 */
	@ApiModelProperty(value="菜单标题")
	private String title;
	/**
	 * 菜单权限标识
	 */
	@ApiModelProperty(value="菜单权限标识")
	private String code;
	/**
	 * 路由URL
	 */
	@ApiModelProperty(value="路由URL")
	private String path;
	/**
	 * 路由名称
	 */
	@ApiModelProperty(value="路由名称")
	private String routerName;
	/**
	 * component地址
	 */
	@ApiModelProperty(value="component地址")
	private String component;
	/**
	 * 重定向地址
	 */
	@ApiModelProperty(value="重定向地址")
	private String redirect;
	/**
	 * 链接跳转目标
	 */
	@ApiModelProperty(value="链接跳转目标")
	private String target;
	/**
	 * 父菜单ID
	 */
	@ApiModelProperty(value="父菜单ID")
	private Integer parentId;
	/**
	 * 图标
	 */
	@ApiModelProperty(value="图标")
	private String icon;
	/**
	 * 排序值
	 */
	@ApiModelProperty(value="排序值")
	private Integer sort;
	/**
	 * 0-开启，1- 关闭
	 */
	@ApiModelProperty(value="0-开启，1- 关闭")
	private Integer keepAlive;
	/**
	 * 是否隐藏路由: 0否,1是
	 */
	@ApiModelProperty(value="是否隐藏路由: 0否,1是")
	private Integer hidden;
	/**
	 * 菜单类型 （0菜单 1按钮）
	 */
	@ApiModelProperty(value="菜单类型 （0菜单 1按钮）")
	private Integer type;
	/**
	 * 逻辑删除标记(0--正常 1--删除)
	 */
	@TableLogic
	@ApiModelProperty(value="逻辑删除标记(0--正常 1--删除)")
	private Integer delFlag;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;





}
