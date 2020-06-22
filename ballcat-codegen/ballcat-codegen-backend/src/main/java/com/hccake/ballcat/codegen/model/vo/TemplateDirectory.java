package com.hccake.ballcat.codegen.model.vo;

import com.hccake.ballcat.common.core.tree.SimpleTreeNode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/21 17:59
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TemplateDirectory extends SimpleTreeNode<Integer> {

	/**
	 * 模板组Id
	 */
	@ApiModelProperty(value = "模板组Id")
	private Integer groupId;
	/**
	 * 文件夹全路径/模板文件名称（支持占位符）
	 */
	@ApiModelProperty(value="文件夹全路径/模板文件名称（支持占位符）")
	private String fileName;
	/**
	 * 文件类型 1：文件夹 2：模板文件
	 */
	@ApiModelProperty(value="文件类型 1：文件夹 2：模板文件")
	private Integer type;

}
