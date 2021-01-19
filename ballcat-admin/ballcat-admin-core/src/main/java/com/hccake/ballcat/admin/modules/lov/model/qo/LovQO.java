package com.hccake.ballcat.admin.modules.lov.model.qo;

import com.hccake.ballcat.admin.modules.lov.enums.HttpMethod;
import com.hccake.ballcat.admin.modules.lov.enums.HttpParamsPosition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lingting 2020-08-12 21:35
 */
@Data
@ApiModel(value = "lov查询对象")
public class LovQO {

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("关键字，唯一，加载lov数据时通过关键字加载")
	private String keyword;

	@ApiModelProperty("获取数据时请求路径")
	private String url;

	@ApiModelProperty("http请求方式")
	private HttpMethod method;

	@ApiModelProperty("http请求参数位置")
	private HttpParamsPosition position;

}
