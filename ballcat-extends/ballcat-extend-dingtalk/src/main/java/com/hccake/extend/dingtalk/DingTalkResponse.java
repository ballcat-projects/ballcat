package com.hccake.extend.dingtalk;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * 钉钉返回信息
 *
 * @author lingting  2020/6/11 0:23
 */
@Getter
@Setter
public class DingTalkResponse {
	public static final String SUCCESS_CODE = "0";
	private String errCode;
	/**
	 * 值为ok表示无异常
	 */
	private String errMsg;
	/**
	 * 钉钉返回信息
	 */
	private String response;
	/**
	 * 是否发送成功
	 */
	private boolean success;

	public static DingTalkResponse getInstance(String res) {
		JSONObject json = JSONUtil.parseObj(res);
		DingTalkResponse response = new DingTalkResponse();
		response.errCode = json.getStr("errcode");
		response.errMsg = json.getStr("errmsg");
		response.response = res;
		response.success = SUCCESS_CODE.equalsIgnoreCase(response.errCode);
		return response;
	}

	@Override
	public String toString() {
		return response;
	}
}
