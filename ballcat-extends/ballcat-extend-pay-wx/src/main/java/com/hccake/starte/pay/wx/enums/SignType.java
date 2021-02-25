package com.hccake.starte.pay.wx.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/1/29 18:14
 */
@Getter
@AllArgsConstructor
public enum SignType {

	/**
	 * 一般用于沙箱环境
	 */
	MD5("MD5"),
	/**
	 * 一般用于正式环境
	 */
	HMAC_SHA256("HMAC-SHA256"),

	;

	private final String str;

	public static SignType of(String str){
		for (SignType e: values()){
			if (e.str.equals(str)){
				return e;
			}
		}
		return null;
	}
}
