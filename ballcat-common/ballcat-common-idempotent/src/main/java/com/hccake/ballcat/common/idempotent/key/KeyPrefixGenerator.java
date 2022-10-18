package com.hccake.ballcat.common.idempotent.key;

/**
 * Key前缀生成器
 *
 * @author lishangbu
 * @date 2022/10/18
 */
public interface KeyPrefixGenerator {

	/**
	 * 生成前缀
	 * @return 生成的前缀
	 */
	String generate();

}
