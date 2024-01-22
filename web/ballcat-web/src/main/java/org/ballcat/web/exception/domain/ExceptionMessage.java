/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.web.exception.domain;

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
	 * 用于筛选重复异常
	 */
	private String key;

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

	/**
	 * hostname
	 */
	private String hostname;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 请求地址
	 */
	private String requestUri;

	/**
	 * 数量自增
	 */
	public ExceptionMessage increment() {
		this.number++;
		return this;
	}

	@Override
	public String toString() {
		return "服务名称：" + this.applicationName + "\nip：" + this.ip + "\nhostname：" + this.hostname + "\n机器地址：" + this.mac
				+ "\n触发时间：" + this.time + "\n请求地址：" + this.requestUri + "\n线程id：" + this.threadId + "\n数量："
				+ this.number + "\n堆栈：" + this.stack;
	}

}
