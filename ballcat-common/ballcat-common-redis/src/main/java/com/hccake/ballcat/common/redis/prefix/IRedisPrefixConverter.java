package com.hccake.ballcat.common.redis.prefix;

import cn.hutool.core.text.CharSequenceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

/**
 * redis key前缀生成器
 *
 * @author huyuanzhi
 */
public interface IRedisPrefixConverter {

	Logger LOGGER = LoggerFactory.getLogger(IRedisPrefixConverter.class);

	/**
	 * 生成前缀
	 * @return 前缀
	 */
	String getPrefix();

	/**
	 * 前置匹配，是否走添加前缀规则
	 * @return 是否匹配
	 */
	boolean match();

	/**
	 * 去除key前缀
	 * @param bytes key字节数组
	 * @return 原始key
	 */
	default byte[] unwrap(byte[] bytes) {
		int wrapLen;
		if (!match() || bytes == null || (wrapLen = bytes.length) == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (CharSequenceUtil.isBlank(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		int originLen = wrapLen - prefixLen;
		byte[] originBytes = new byte[originLen];
		System.arraycopy(bytes, prefixLen, originBytes, 0, originLen);
		return originBytes;
	}

	/**
	 * 给key加上固定前缀
	 * @param bytes 原始key字节数组
	 * @return 加前缀之后的key
	 */
	default byte[] wrap(byte[] bytes) {
		int originLen;
		if (!match() || bytes == null || (originLen = bytes.length) == 0) {
			return bytes;
		}
		String prefix = getPrefix();
		if (CharSequenceUtil.isBlank(prefix)) {
			LOGGER.warn("prefix converter is enabled,but method getPrefix returns blank result,check your implement!");
			return bytes;
		}
		byte[] prefixBytes = prefix.getBytes(StandardCharsets.UTF_8);
		int prefixLen = prefixBytes.length;
		byte[] wrapBytes = new byte[prefixLen + originLen];
		System.arraycopy(prefixBytes, 0, wrapBytes, 0, prefixLen);
		System.arraycopy(bytes, 0, wrapBytes, prefixLen, originLen);
		return wrapBytes;
	}

}
