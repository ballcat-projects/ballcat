package com.hccake.ballcat.autoconfigure.idempotent;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 幂等属性配置
 *
 * @author lishangbu
 * @date 2022/10/18
 */
@Setter
@Getter
@ConfigurationProperties(IdempotentProperties.PREFIX)
public class IdempotentProperties {

	public static final String PREFIX = "ballcat.idempotent";

	private KeyStoreType keyStoreType = KeyStoreType.MEMORY;

	/**
	 * 存储方式
	 */
	public enum KeyStoreType {

		/**
		 * 内存存储
		 */
		MEMORY,
		/**
		 * redis存储
		 */
		REDIS

	}

}
