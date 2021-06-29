package org.springframework.data.redis.connection.lettuce;

import io.lettuce.core.protocol.ProtocolKeyword;

import java.nio.charset.StandardCharsets;

/**
 * 布隆过滤器的操作命令类
 * @see <a href="https://oss.redislabs.com/redisbloom/Bloom_Commands/">redisbloom command
 * doc</a>
 * @author hccake
 */
public enum BloomCommandEnum implements ProtocolKeyword {

	/**
	 * 创建一个空的 bloomFilter, 可以指定初始容量和预期错误率
	 */
	RESERVE("BF.RESERVE"),
	/**
	 * 将一个元素添加进 bloomFilter，如果 filter 不存在，则创建一个 filter
	 */
	ADD("BF.ADD"),
	/**
	 * 将一个元素或多个元素添加进 bloomFilter，如果 filter 不存在，则创建一个 filter
	 */
	MADD("BF.MADD"),
	/**
	 * 确定一个元素是否在 bloomFilter 中存在
	 */
	EXISTS("BF.EXISTS"),
	/**
	 * 确定一个元素或多个元素是否在 bloomFilter 中存在
	 */
	MEXISTS("BF.MEXISTS"),
	/**
	 * 是 BF.RESERVE 和 BF.ADD 的语法糖。 如果 filter 不存在，则使用相关参数创建一个新的过滤器(参见 BF.RESERVE)。
	 * 接下来，插入所有项目。
	 */
	INSERT("BF.INSERT"),
	/**
	 * 获取 filter 的相关信息
	 */
	INFO("BF.INFO");

	private final String command;

	private final byte[] bytes;

	BloomCommandEnum(String command) {
		this.command = command;
		bytes = command().getBytes(StandardCharsets.US_ASCII);
	}

	public String command() {
		return this.command;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

}
