package com.hccake.starter.sms;

/**
 * 短信发送 接口类
 *
 * @author lingting 2020/4/26 9:55
 */
public interface SmsSender<T, R> {

	/**
	 * 发送短信
	 * @param p 参数配置
	 * @return boolean
	 */
	R send(T p);

}
