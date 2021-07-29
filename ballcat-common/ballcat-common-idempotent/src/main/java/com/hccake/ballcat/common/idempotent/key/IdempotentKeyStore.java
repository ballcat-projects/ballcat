package com.hccake.ballcat.common.idempotent.key;

/**
 * @author hccake
 */
public interface IdempotentKeyStore {

	/**
	 * 当不存在有效 key 时将其存储下来
	 * @param key idempotentKey
	 * @param duration key的有效时长
	 * @return boolean true: 存储成功 false: 存储失败
	 */
	boolean saveIfAbsent(String key, long duration);

	/**
	 * 删除 key
	 * @param key idempotentKey
	 */
	void remove(String key);

}
