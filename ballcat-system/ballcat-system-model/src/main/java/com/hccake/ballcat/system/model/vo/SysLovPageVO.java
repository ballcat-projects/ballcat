package com.hccake.ballcat.system.model.vo;

import com.hccake.ballcat.system.enums.HttpMethodEnum;
import com.hccake.ballcat.system.enums.HttpParamsPositionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * @author lingting 2020-08-12 21:35
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "lov vo")
public class SysLovPageVO {

	@ApiModelProperty("编号")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("关键字，唯一，加载lov数据时通过关键字加载")
	private String keyword;

	@NotBlank
	@ApiModelProperty("获取数据时请求路径")
	private String url;

	@NotBlank
	@ApiModelProperty("http请求方式")
	private HttpMethodEnum method;

	@NotBlank
	@ApiModelProperty("http请求参数位置")
	private HttpParamsPositionEnum position;

	@ApiModelProperty("数据的key")
	private String key;

	@ApiModelProperty("固定请求参数，请设置 jsonString, 默认值 {}")
	private String fixedParams;

	@ApiModelProperty("是否需要多选")
	private Boolean multiple;

	@ApiModelProperty("是否需要返回数据, false则不会有确定按钮")
	private Boolean ret;

	@ApiModelProperty("返回数据的字段")
	private String retField;

	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
