package com.hccake.ballcat.system.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 字典表
 *
 * @author hccake
 * @date 2020-03-26 18:40:20
 */
@Data
@Schema(title = "字典表")
public class SysDictPageVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 编号
	 */
	@Schema(title = "编号")
	private Integer id;

	/**
	 * 标识
	 */
	@Schema(title = "标识")
	private String code;

	/**
	 * 名称
	 */
	@Schema(title = "名称")
	private String title;

	/**
	 * Hash值
	 */
	@Schema(title = "Hash值")
	private String hashCode;

	/**
	 * 备注
	 */
	@Schema(title = "备注")
	private String remarks;

	/**
	 * 数据类型
	 */
	@Schema(title = "数据类型", description = "1:Number 2:String 3:Boolean")
	private Integer valueType;

	/**
	 * 创建时间
	 */
	@Schema(title = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(title = "更新时间")
	private LocalDateTime updateTime;

}
