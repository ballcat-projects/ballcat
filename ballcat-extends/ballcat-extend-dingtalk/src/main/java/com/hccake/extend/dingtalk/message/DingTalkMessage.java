package com.hccake.extend.dingtalk.message;

/**
 * @author lingting 2020/6/11 21:58
 */
public interface DingTalkMessage {

	/**
	 * 生成钉钉消息发送参数
	 * @return 钉钉文档要求的 jsonString
	 */
	String generate();

}
