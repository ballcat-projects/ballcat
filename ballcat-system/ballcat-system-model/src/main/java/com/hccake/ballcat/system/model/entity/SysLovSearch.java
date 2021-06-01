package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.system.enums.TagEnum;
import com.hccake.ballcat.system.typehandler.ListLovSelectOptionTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author lingting 2020/7/5 16:04
 */
@Data
@TableName(value = "sys_lov_search", autoResultMap = true)
@Accessors(chain = true)
@ApiModel(value = "lov搜索模块")
public class SysLovSearch {

	@TableId
	@ApiModelProperty("编号")
	private Long id;

	@ApiModelProperty("关键字，唯一，通过关键字关联lov")
	private String keyword;

	@NotEmpty
	@ApiModelProperty("标签文字")
	private String label;

	@NotEmpty
	@ApiModelProperty("字段")
	private String field;

	@ApiModelProperty("placeholder")
	private String placeholder;

	@ApiModelProperty("html 标签")
	private TagEnum tag;

	@TableField(typeHandler = ListLovSelectOptionTypeHandler.class)
	@ApiModelProperty("tag=SELECT时的选项")
	private List<LovSelectOptions> options;

	@ApiModelProperty("tag=INPUT_NUMBER时的选项，设置数字最小值")
	private Integer min;

	@ApiModelProperty("tag=INPUT_NUMBER时的选项，设置数字最大值")
	private Integer max;

	@ApiModelProperty("tag=DICT_SELECT时的选项，设置dict-code")
	private String dictCode;

	@ApiModelProperty("是否自定义html")
	private Boolean custom;

	@ApiModelProperty("如果 custom=true 则当前值不能为空")
	private String html;

	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
