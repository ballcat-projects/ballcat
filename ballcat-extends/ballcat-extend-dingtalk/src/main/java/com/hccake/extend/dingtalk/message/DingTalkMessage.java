package com.hccake.extend.dingtalk.message;

/**
 * @author lingting  2020/6/11 21:58
 */
public interface DingTalkMessage {
	/**
	 * 生成钉钉消息发送参数
	 *
	 * @return 钉钉文档要求的 jsonString
	 * @author lingting  2020-06-12 19:56:54
	 */
	String generate();
}
