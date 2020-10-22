package com.hccake.starter.sms;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.SecureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hccake.ballcat.common.core.util.SpringUtil;
import com.hccake.starter.sms.constant.SmsConstants;
import com.hccake.starter.sms.properties.SmsProperties;
import com.hccake.starter.sms.properties.extra.Account;
import com.hccake.starter.sms.properties.extra.Tencent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

/**
 * @author lingting 2020/4/26 11:40
 */
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
	 * 短信模板参数
	 */
	private List<Object> templateParam;

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 使用此号码发送短信
	 */
	private String usePhone;

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

	/**
	 * 生成参数-腾讯云平台
	 */
	@SneakyThrows
	public String generateTencentParams(Tencent tencent) {
		Map<String, Object> json = new HashMap<>(5);
		json.put("PhoneNumberSet", phoneNumbers);

		json.put("SmsSdkAppid", tencent.getSdkId());

		if (tencent.getTemplateId() != null) {
			json.put("TemplateID", tencent.getTemplateId());
		}

		if (StrUtil.isNotEmpty(tencent.getSign())) {
			json.put("Sign", tencent.getSign());
		}
		if (templateParam.size() != 0) {
			json.put("TemplateParamSet", templateParam);
		}
		return SmsSenderResult.getObjectMapper().writeValueAsString(json);
	}

	/**
	 * 生成参数-码平台
	 */
	public String generateTianYiHongParams(SmsProperties properties) {
		Account account = properties.getAccounts().get(country);
		String dateTime = DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss");
		return URLUtil.encode("account=" + account.getAccount() + "&sign="
				+ SecureUtil.md5(account.getAccount() + account.getPassword() + dateTime) + "&datetime=" + dateTime
				+ "&numbers=" + phoneNumbers.iterator().next() + "&content=" + content
				+ (country.equals(SmsConstants.SENDER_ID_COUNTRY) ? "&senderid=THRONE" : ""));
	}

	@Override
	@SneakyThrows
	public String toString() {
		return SmsSenderResult.getObjectMapper().writeValueAsString(this);
	}

}
