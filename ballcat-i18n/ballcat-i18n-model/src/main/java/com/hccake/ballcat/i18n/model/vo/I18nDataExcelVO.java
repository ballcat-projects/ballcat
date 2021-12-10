package com.hccake.ballcat.i18n.model.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * 国际化信息Excel映射对象
 *
 * @author hccake 2021-08-06 10:48:25
 */
@Data
@Schema(title = "国际化信息Excel映射对象")
public class I18nDataExcelVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 语言标签
	 */
	@ExcelProperty(value = "{i18nMessage.languageTag}", index = 0)
	@Schema(title = "语言标签")
	private String languageTag;

	/**
	 * 国际化标识
	 */
	@ExcelProperty(value = "{i18nMessage.code}", index = 1)
	@Schema(title = "国际化标识")
	private String code;

	/**
	 * 文本值，可以使用 { } 加角标，作为占位符
	 */
	@ExcelProperty(value = "{i18nMessage.message}", index = 2)
	@Schema(title = "文本值，可以使用 { } 加角标，作为占位符")
	private String message;

	/**
	 * 备注
	 */
	@ExcelProperty(value = "{i18nData.remarks}", index = 3)
	@Schema(title = "备注")
	private String remarks;

}