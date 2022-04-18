package com.hccake.ballcat.common.code;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 验证码配置
 *
 * @author xm.z
 */
@Data
@ConfigurationProperties(prefix = "healthy.security")
public class ValidateCodeProperties {

	/**
	 * 图片验证码配置
	 */
	@NestedConfigurationProperty
	private ImageCodeProperties image = new ImageCodeProperties();

	/**
	 * 短信验证码配置
	 */
	@NestedConfigurationProperty
	private SmsCodeProperties sms = new SmsCodeProperties();

	/**
	 * 短信验证码相关配置项
	 */
	@Data
	public static class SmsCodeProperties {

		/**
		 * 验证码长度
		 */
		private int length = 6;

		/**
		 * 过期时间(秒)
		 */
		private int expireIn = 60;

		/**
		 * 要拦截的url集合
		 */
		private Set<String> urls = new LinkedHashSet<>();

	}

	/**
	 * 图片验证码配置项
	 */
	@Data
	public static class ImageCodeProperties {

		/**
		 * 图片宽
		 */
		private int width = 200;

		/**
		 * 图片高
		 */
		private int height = 50;

		/**
		 * 验证码长度
		 */
		private int length = 4;

		/**
		 * 过期时间(秒)
		 */
		private int expireIn = 60;

		/**
		 * 要拦截的url集合
		 */
		private Set<String> urls = new LinkedHashSet<>();

	}

}