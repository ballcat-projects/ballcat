package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@TableName("sys_dict")
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "字典表")
public class SysDict extends Model<SysDict> {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId
    @ApiModelProperty(value = "编号")
    private Integer id;
    /**
     * 标识
     */
    @ApiModelProperty(value = "标识")
    private String code;
    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private String title;
    /**
     * Hash值
     */
    @ApiModelProperty(value = "Hash值")
    private String hashCode;
    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remarks;
    /**
     * 可编辑的
     */
    @ApiModelProperty(value = "1：是 0：否")
    private Integer editable;
	/**
	 * 逻辑删除标识，已删除:0，未删除：删除时间戳
	 */
	@TableLogic
	@ApiModelProperty(value="逻辑删除标识，已删除:0，未删除：删除时间戳")
	private Long deleted;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建时间")
    private LocalDateTime createTime;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;
}
