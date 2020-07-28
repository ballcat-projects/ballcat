package com.hccake.ballcat.admin.modules.lov.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author lingting 2020/7/5 16:04
 */
@Data
@TableName("sys_lov_body")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "lov主体模块")
public class LovBody extends Model<LovBody> {

	@TableId
	@ApiModelProperty("编号")
	private Long id;

	@ApiModelProperty("所属lov")
	private Long lovId;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("字段, 同一lov下，field不可重复`")
	private String field;

	@Pattern(regexp = "^\\{.*}")
	@ApiModelProperty("自定义属性，请设置 jsonString, 默认值 {}")
	private String property;

	@ApiModelProperty("是否自定义html")
	private boolean custom;

	@ApiModelProperty("如果 custom=true 则当前值不能为空")
	private String html;

	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
