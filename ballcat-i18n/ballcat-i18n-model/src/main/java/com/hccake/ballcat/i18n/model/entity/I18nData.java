package com.hccake.ballcat.i18n.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 国际化信息
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Getter
@Setter
@ToString
@TableName("i18n_data")
@ApiModel(value = "国际化信息")
public class I18nData {

	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	@ApiModelProperty(value = "ID")
	private Integer id;

	/**
	 * 语言标签
	 */
	@ApiModelProperty(value = "语言标签")
	private String languageTag;

	/**
	 * 国际化标识
	 */
	@ApiModelProperty(value = "国际化标识")
	private String code;

	/**
	 * 文本值，可以使用 { } 加角标，作为占位符
	 */
	@ApiModelProperty(value = "文本值，可以使用 { } 加角标，作为占位符")
	private String message;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;

		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		I18nData i18nData = (I18nData) o;
		return languageTag.equals(i18nData.languageTag) && code.equals(i18nData.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(languageTag, code);
	}

}
