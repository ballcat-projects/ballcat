package com.hccake.starter.sms;

import com.hccake.starter.sms.properties.extra.Tencent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * @author lingting 2020/4/26 11:40
 */
@ToString
@Getter
@Setter
@Accessors(chain = true)
public class SmsSenderParams {

	/**
	 * 下发手机号码，采用 e.164 标准，格式为+[国家或地区码][手机号]，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
	 * 例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。
	 */
	private Set<String> phoneNumbers;

	/**
	 * 短信模板参数 腾讯云专用
	 *
	 * @see Tencent
	 */
	private List<Object> templateParam;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 国家代码 比如 CN
	 */
	private String country;

	{
		// 初始化参数
		this.phoneNumbers = new HashSet<>();
		this.templateParam = new ArrayList<>();
	}

	/**
	 * 添加电话
	 */
	public SmsSenderParams addPhone(String phone) {
		this.phoneNumbers.add(phone);
		return this;
	}

	/**
	 * 添加模板参数
	 *
	 * @author lingting 2020-04-26 11:07:36
	 */
	public SmsSenderParams addTemplateParam(Object param) {
		this.templateParam.add(param);
		return this;
	}

}
