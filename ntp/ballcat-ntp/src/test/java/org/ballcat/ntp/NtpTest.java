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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting 2022/11/18 11:51
 */
class NtpTest {

	@Test
	void ntp() {
		Ntp ntp = new Ntp(NtpCn.DEFAULT_TIME_SERVER);
		System.out.println("服务时间: " + ntp.currentMillis());
		System.out.println("系统时间: " + System.currentTimeMillis());
		Assertions.assertNotNull(ntp);
	}

}
