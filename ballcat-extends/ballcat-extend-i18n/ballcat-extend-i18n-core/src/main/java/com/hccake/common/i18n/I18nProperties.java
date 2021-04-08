package com.hccake.common.i18n;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * i18n properties
 *
 * @author Yakir
 */
@Data
@ConfigurationProperties(prefix = "ballcat.i18n")
public class I18nProperties {

	/**
	 * 系统名称
	 */
	private String systemName = "test-item";

	/**
	 * 缓存空值标记
	 */
	private String nullValue = "N_V";

	/**
	 * 执行器 主要用来对数据做附加操作
	 */
	private String executor = "simple";

	/**
	 * 生成器
	 */
	private Generate generate = new Generate();

	/**
	 * 缓存设置
	 */
	private Cache cache = new Cache();

	@Data
	public class Generate {

		/**
		 * 生成器定界符
		 */
		private String delimiter = ":";

	}

	@Data
	public class Cache {

		/**
		 * 缓存类型
		 */
		private String type = "local";

		/**
		 * 过期时间(s) -1 表示永不过期
		 */
		private Long expire = -1L;

	}

	public boolean isNullValue(String val) {
		return this.nullValue.equals(val);
	}

}
