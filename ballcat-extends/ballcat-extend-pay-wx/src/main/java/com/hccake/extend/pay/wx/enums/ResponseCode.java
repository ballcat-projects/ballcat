package com.hccake.extend.pay.wx.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回code
 * @author lingting 2021/2/1 11:31
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 失败
	 */
	FAIL,
	/**
	 * 异常
	 */
	ERROR,

	;

	@JsonCreator
	public static ResponseCode of(String status) {
		switch (status) {
		case "SUCCESS":
			return SUCCESS;
		case "FAIL":
			return FAIL;
		default:
			return ERROR;
		}
	}

}
