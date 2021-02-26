package com.hccake.starter.sms;

import cn.hutool.core.convert.Convert;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.common.util.json.TypeReference;
import com.hccake.starter.sms.enums.TypeEnum;
import java.util.Map;
import java.util.Set;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;

/**
 * 短信发送结果
 *
 * @author lingting 2020/4/26 13:22
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
	 * @see com.hccake.starter.sms.constant.SmsConstants
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
	 * @return com.hccake.starter.sms.domain.SendResult
	 * @author lingting 2020-04-26 13:43:39
	 */
	@SneakyThrows
	public static SmsSenderResult generateException(TypeEnum platform, Set<String> phoneNumbers, String id,
			Throwable e) {
		SmsSenderResult result = new SmsSenderResult();
		result.success = false;
		result.msg = "短信发送失败，出现异常:" + e.getMessage() + "," + id;
		result.target = JsonUtils.toJson(phoneNumbers);
		result.platform = platform.name();
		return result;
	}

	/**
	 * 生成结果数据
	 * @param resp 请求的返回结果
	 * @param phoneNumbers 目标号码
	 */
	@SneakyThrows
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

	@SneakyThrows
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
		if (Convert.toInt(map.get(TIAN_YI_HONG_STATUS), -1) < 0) {
			result.success = false;
			result.msg = Convert.toStr(map.get(TIAN_YI_HONG_MSG), "获取错误信息失败!");
		}
		return result;
	}

	@SneakyThrows
	@Override
	public String toString() {
		return JsonUtils.toJson(this);
	}

}
