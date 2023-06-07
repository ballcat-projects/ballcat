/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballcat.sms;

import lombok.Getter;
import lombok.experimental.Accessors;
import org.ballcat.common.constant.Symbol;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.common.util.json.TypeReference;
import org.ballcat.sms.constant.SmsConstants;
import org.ballcat.sms.enums.TypeEnum;

import java.util.Map;
import java.util.Set;

/**
 * 短信发送结果
 *
 * @author lingting 2020/4/26 13:22
 * @author 疯狂的狮子Li 2022-04-21
 */
@Getter
@Accessors(chain = true)
public class SmsSenderResult {

	/**
	 * 状态字段名
	 */
	private static final String TIAN_YI_HONG_STATUS = "status";

	/**
	 * 信息字段名
	 */
	private static final String TIAN_YI_HONG_MSG = "desc";

	/**
	 * 短信平台
	 *
	 * @see SmsConstants
	 */
	protected String platform;

	/**
	 * 发送的目标
	 */
	protected String target;

	/**
	 * 是否发送成功
	 */
	protected boolean success;

	/**
	 * 提示信息
	 */
	protected String msg;

	/**
	 * 请求的详细信息-各平台自定义
	 */
	protected String req;

	/**
	 * 返回结果信息
	 */
	protected String res;

	private SmsSenderResult() {

	}

	/**
	 * 出现异常返回结果
	 * @param platform 平台
	 * @param phoneNumbers 目标手机号
	 * @param id 异常id
	 * @param e 异常信息
	 * @return 短信发送结果
	 */
	public static SmsSenderResult generateException(TypeEnum platform, Set<String> phoneNumbers, String id,
			Throwable e) {
		SmsSenderResult result = new SmsSenderResult();
		result.success = false;
		result.msg = "短信发送失败，出现异常:" + e.getMessage() + Symbol.COMMA + id;
		result.target = JsonUtils.toJson(phoneNumbers);
		result.platform = platform.name();
		return result;
	}

	/**
	 * 生成结果数据
	 * @param resp 请求的返回结果
	 * @param phoneNumbers 目标号码
	 */
	public static SmsSenderResult generate(String resp, String req, Set<String> phoneNumbers) {
		SmsSenderResult result = new SmsSenderResult();
		result.res = resp;
		// 没有异常就是成功!
		result.success = true;
		result.platform = TypeEnum.TENCENT.name();
		result.target = JsonUtils.toJson(phoneNumbers);
		result.req = req;
		return result;
	}

	public static SmsSenderResult generateTianYiHong(String resp, String req, Set<String> phoneNumbers) {
		SmsSenderResult result = new SmsSenderResult();
		result.res = resp;
		// 没有异常就是成功!
		result.success = true;
		result.platform = TypeEnum.TIAN_YI_HONG.name();
		result.target = JsonUtils.toJson(phoneNumbers);
		result.req = req;

		Map<String, Object> map = JsonUtils.toObj(resp, new TypeReference<Map<String, Object>>() {
		});

		final Object status = map.getOrDefault(TIAN_YI_HONG_STATUS, "-1");
		if (null == status || Integer.parseInt((String) status) < 0) {
			result.success = false;
			final Object msg = map.get(TIAN_YI_HONG_MSG);
			result.msg = msg == null ? "获取错误信息失败!" : (String) msg;
		}
		return result;
	}

	public static SmsSenderResult generateAliyun(String resp, String req, Set<String> phoneNumbers) {
		SmsSenderResult result = new SmsSenderResult();
		result.res = resp;
		// 没有异常就是成功!
		result.success = true;
		result.platform = TypeEnum.ALIYUN.name();
		result.target = JsonUtils.toJson(phoneNumbers);
		result.req = req;
		return result;
	}

	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
