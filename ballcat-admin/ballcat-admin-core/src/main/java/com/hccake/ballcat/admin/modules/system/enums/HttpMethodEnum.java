package com.hccake.ballcat.admin.modules.system.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于设置 请求方式
 *
 * @author lingting 2020/7/5 16:18
 */
@Getter
@AllArgsConstructor
public enum HttpMethodEnum {

	/**
	 * http 请求方式
	 */
	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,;

}
