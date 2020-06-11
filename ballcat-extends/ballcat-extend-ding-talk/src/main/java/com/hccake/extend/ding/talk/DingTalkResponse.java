package com.hccake.extend.ding.talk;

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
	private String errCode;
	/**
	 * 值为ok表示无异常
	 */
	private String errMsg;
	/**
	 * 钉钉返回信息
	 */
	private String response;

	public static DingTalkResponse getInstance(String res) {
		JSONObject json = JSONUtil.parseObj(res);
		DingTalkResponse response = new DingTalkResponse();
		response.errCode = json.getStr("errcode");
		response.errMsg = json.getStr("errmsg");
		response.response = res;
		return response;
	}

	@Override
	public String toString() {
		return response;
	}
}
