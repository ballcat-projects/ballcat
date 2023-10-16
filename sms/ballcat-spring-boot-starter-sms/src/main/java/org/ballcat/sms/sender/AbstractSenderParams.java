package org.ballcat.sms.sender;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lingting 2023-10-16 19:34
 */
@Getter
@Setter
public abstract class AbstractSenderParams<P extends AbstractSenderParams<P>> {

	/**
	 * 下发手机号码，采用 e.164 标准，格式为+[国家或地区码][手机号]，单次请求最多支持200个手机号且要求全为境内手机号或全为境外手机号。
	 * 例如：+8613711112222， 其中前面有一个+号 ，86为国家码，13711112222为手机号。
	 */
	private Set<String> phoneNumbers = new HashSet<>();

	/**
	 * 短信内容
	 */
	private String content;

	/**
	 * 国家代码 比如 CN
	 */
	private String country;

	/**
	 * 添加手机号
	 */
	public P addPhone(String phone) {
		this.phoneNumbers.add(phone);
		return (P) this;
	}

}
