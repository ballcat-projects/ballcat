package com.hccake.ballcat.common.idempotent.key;

/**
 * <p>
 * 幂等Key存储
 * </p>
 *
 * 消费过的幂等 key 记录下来，再下次消费前校验 key 是否已记录，从而拒绝执行
 *
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
