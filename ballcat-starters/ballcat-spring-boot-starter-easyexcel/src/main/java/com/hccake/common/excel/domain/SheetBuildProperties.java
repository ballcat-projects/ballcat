package com.hccake.common.excel.domain;

import com.hccake.common.excel.annotation.Sheet;
import com.hccake.common.excel.head.HeadGenerator;
import lombok.Data;

/**
 * Sheet Build Properties
 *
 * @author chengbohua
 */
@Data
public class SheetBuildProperties {

	/**
	 * sheet 编号
	 */
	private int sheetNo = -1;

	/**
	 * sheet name
	 */
	private String sheetName;

	/**
	 * 包含字段
	 */
	private String[] includes = new String[0];

	/**
	 * 排除字段
	 */
	private String[] excludes = new String[0];

	/**
	 * 头生成器
	 */
	private Class<? extends HeadGenerator> headGenerateClass = HeadGenerator.class;

	public SheetBuildProperties(Sheet sheetAnnotation) {
		this.sheetNo = sheetAnnotation.sheetNo();
		this.sheetName = sheetAnnotation.sheetName();
		this.includes = sheetAnnotation.includes();
		this.excludes = sheetAnnotation.excludes();
		this.headGenerateClass = sheetAnnotation.headGenerateClass();
	}

	public SheetBuildProperties(int index) {
		this.sheetNo = index;
		this.sheetName = "sheet" + (sheetNo + 1);
	}

}
