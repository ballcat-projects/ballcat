package com.hccake.ballcat.admin.modules.lov.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hccake.ballcat.admin.modules.lov.enums.Tag;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author lingting 2020/7/5 16:04
 */
@Data
@TableName("sys_lov_search")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "lov搜索模块")
public class LovSearch extends Model<LovSearch> {

	@TableId
	@ApiModelProperty("编号")
	private Long id;

	@NotEmpty
	@ApiModelProperty("标签文字")
	private String label;

	@NotEmpty
	@ApiModelProperty("字段")
	private String field;

	@ApiModelProperty("placeholder")
	private String placeholder;

	@ApiModelProperty("标签")
	private Tag tag;

	@Pattern(regexp = "\\[(\\{['|\"]key.*['|\"]value.*['|\"]label.*})*]$")
	@ApiModelProperty("tag=SELECT时的选项, 请设置jsonString , 默认值 [] 示例: [{key:field, value:值, label:标签}]")
	private String options;

	@ApiModelProperty("tag=INPUT_NUMBER时的选项，设置数字最小值")
	private Integer min;

	@ApiModelProperty("tag=INPUT_NUMBER时的选项，设置数字最大值")
	private Integer max;

	@ApiModelProperty("是否自定义html")
	private boolean custom;

	@ApiModelProperty("如果 custom=true 则当前值不能为空")
	private String html;

	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
