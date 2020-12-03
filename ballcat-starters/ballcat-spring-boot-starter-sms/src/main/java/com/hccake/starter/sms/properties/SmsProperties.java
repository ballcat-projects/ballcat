package com.hccake.starter.sms.properties;

import com.hccake.starter.sms.enums.TypeEnum;
import com.hccake.starter.sms.properties.extra.Account;
import com.hccake.starter.sms.properties.extra.Tencent;
import com.hccake.starter.sms.properties.extra.TianYiHong;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.Map;

/**
 * @author lingting 2020/4/26 9:43
 */
@Data
@ConfigurationProperties(prefix = "ballcat.sms")
public class SmsProperties {

	/**
	 * 类型
	 */
	private TypeEnum type = TypeEnum.CUSTOM;

	/**
	 * 请求路径
	 */
	private String url;

	/**
	 * app id
	 */
	private String id;

	/**
	 * app key
	 */
	private String key;

	/**
	 * 部分平台需要使用账号密码，都在这里配置
	 */
	private Map<String, Account> accounts;

	/**
	 * 腾讯云所需额外参数
	 */
	@NestedConfigurationProperty
	private Tencent tencent;

	/**
	 * 码平台所用发送短信所需额外参数
	 */
	@NestedConfigurationProperty
	private TianYiHong tianYiHong;

}
