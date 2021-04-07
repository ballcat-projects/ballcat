package com.hccake.common.i18n.generate;

/**
 * 缓存key生成器
 * @author Yakir
 */
public interface KeyGenerate {

	/**
	 * 缓存 key 生成
	 * @param params 参数数组
	 * @return 指定分隔符字符串
	 */
	String generateKey(String... params);

}
