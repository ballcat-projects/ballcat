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

package org.ballcat.ntp;

/**
 * 中国 ntp 类
 *
 * @author lingting 2023/2/1 14:10
 */
public final class NtpCn {

	private NtpCn() {
	}

	public static final String DEFAULT_TIME_SERVER = "ntp.aliyun.com";

	private static final Ntp INSTANCE = new Ntp(DEFAULT_TIME_SERVER);

	public static long currentTimeMillis() {
		return INSTANCE.currentMillis();
	}

	/**
	 * 几秒后的毫秒级时间戳
	 * @param seconds 秒
	 * @return 时间戳
	 */
	public static long plusSeconds(long seconds) {
		return currentTimeMillis() + (seconds * 1000);
	}

}
