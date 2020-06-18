package com.hccake.ballcat.codegen.model.qo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/17 15:21
 */
@Data
@ApiModel(value = "表信息查询对象")
public class TableInfoQO {

	/**
	 * 表名
	 */
	private String tableName;
}
