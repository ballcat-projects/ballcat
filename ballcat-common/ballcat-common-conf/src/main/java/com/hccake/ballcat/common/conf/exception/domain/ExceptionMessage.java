package com.hccake.ballcat.common.conf.exception.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 异常通知消息
 *
 * @author lingting 2020/6/12 16:07
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExceptionMessage {

	/**
	 * 消息
	 */
	private String message;

	/**
	 * 数量
	 */
	private int number;

	/**
	 * 堆栈
	 */
	private String stack;

	/**
	 * 最新的触发时间
	 */
	private String time;

	/**
	 * 机器地址
	 */
	private String mac;

	/**
	 * 线程id
	 */
	private long threadId;

	/**
	 * 服务名
	 */
	private String applicationName;

	@Override
	public String toString() {
		return "服务名称：" + applicationName + "\n机器地址：" + mac + "\n触发时间：" + time + "\n线程id：" + threadId + "\n数量：" + number
				+ "\n堆栈：" + stack;
	}

}
