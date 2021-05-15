package com.hccake.ballcat.common.util;

import com.hccake.ballcat.common.charset.GSMCharset;

import java.nio.charset.StandardCharsets;

/**
 * 短信的工具类
 *
 * @author hccake
 */
public class SmsUtils {

	/**
	 * 短信载荷可用字节数
	 */
	public static final int SMS_PAYLOAD_BYTE_NUM = 140;

	/**
	 * 在 GSM-7 编码下，短信的最大有效字数：（140 * 8） / 7 = 160 GSM-7 编码使用 7 个 bit 表示一个标准字符，对于 €^ {} []〜|
	 * 这些扩展字符会使用 2 个 7bit位 展示
	 */
	public static final int MAX_WORLD_NUM_IN_GSM = 160;

	/**
	 * 在 UCS-2 编码下，长短信的各部分消息需要占用 7 个bit来记录 UDH
	 */
	public static final int MAX_WORLD_NUM_IN_UCS2 = 70;

	/**
	 * UDH 占用 6 Byte / 48 bit
	 */
	public static final int UDH_BYTE_NUM = 6;

	/**
	 * 根据短信内容，获得对应的短信条数
	 *
	 * 每条短信的有效载荷为 140 个字节，如果消息文本长于 140 字节，则将会串联成为多条消息，又称长短信。
	 * 长短信的各部分消息的载荷中会划分一部分字节用于创建用户数据头（UDH），用于接受设备对接收到的消息进行排序处理。
	 *
	 * UDH占用 6个字节 或 48位。这减少了每个消息部分中可以包含多少个字符的空间。
	 * @param smsContent 短信内容
	 * @return 短信条数
	 */
	public static int smsNumber(String smsContent) {
		int wordsNum = GSMCharset.need7bitsNum(smsContent);
		if (wordsNum == 0) {
			return 0;
		}

		// wordsNum > 0, 表示当前短信支持 GSM-7 编码
		if (wordsNum > 0) {
			// 短短信没有 UDH，在 GSM-7 编码下，最大支持 160 个字符
			if (wordsNum <= MAX_WORLD_NUM_IN_GSM) {
				return 1;
			}
			// 长短信，需要占用 48 个 bit，所以最大只能支持 （1120 - 48）/ 7 = 153 个字符
			return wordsNum % 153 == 0 ? wordsNum / 153 : wordsNum / 153 + 1;
		}

		// 当不符合 GSM-7 编码时，使用 UCS-2 编码(默认大端序)
		// UTF-16 兼容 UCS-2，所以这里可以用 UTF_16BE 来解码获得字节
		byte[] bytes = smsContent.getBytes(StandardCharsets.UTF_16BE);
		// 两个字节表示一个字符
		wordsNum = bytes.length / 2;
		// 短短信没有 UDH，在 UCS-2 编码下，最大支持 70 个字符
		if (wordsNum <= MAX_WORLD_NUM_IN_UCS2) {
			return 1;
		}
		// 长短信，需要占用 6 个字节，所以最大只能支持 （140 - 6）/ 2 = 67 个字符
		return wordsNum % 67 == 0 ? wordsNum / 67 : wordsNum / 67 + 1;

	}

}
