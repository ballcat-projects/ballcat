package com.hccake.ballcat.admin.modules.lov.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用于设置 请求方式
 *
 * @author lingting 2020/7/5 16:18
 */
@Getter
@AllArgsConstructor
public enum HttpMethod implements IEnum<String> {

	/**
	 * http 请求方式
	 */
	GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE,;

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public String toString() {
		return name();
	}

}
