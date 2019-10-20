package com.hccake.ballcat.admin.modules.sys.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 14:19
 */
@Data
public class PermissionVO {

    /**
     * 菜单ID
     */
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
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;




    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /**
     * permissionId 相同则相同
     * @param obj
     * @return
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PermissionVO) {
            Integer permissionId = ((PermissionVO) obj).getId();
            return id.equals(permissionId);
        }
        return super.equals(obj);
    }

}
