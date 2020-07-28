package com.hccake.ballcat.admin.modules.lov.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 请求参数存放位置
 *
 * @author lingting 2020/7/5 16:23
 */
@Getter
@AllArgsConstructor
public enum HttpParamsPosition implements IEnum<String> {

	/**
	 * 定义http请求时 参数设置的位置
	 */
	DATA, PARAMS,;

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public String toString() {
		return name().toLowerCase();
	}

}
