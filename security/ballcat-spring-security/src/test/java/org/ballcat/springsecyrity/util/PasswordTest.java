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

package org.ballcat.springsecyrity.util;

import org.ballcat.springsecurity.util.PasswordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author hccake
 */
class PasswordTest {

	PasswordEncoder PASSWORD_ENCODER = PasswordUtils.createDelegatingPasswordEncoder();

	/**
	 * 密码匹配时，带匹配的密文密码如果使用{算法类型}做为前缀，则使用指定算法进行匹配测试。 如果没有前缀，默认使用 bcrypt 算法
	 */
	@Test
	void matchTest() {
		// 明文密码
		String rawPassword = "a123456";

		// 不指定加密算法时，默认使用 bcrypt 加密算法
		String defaultEncodedPassword = "$2a$10$IRAHstZa7wgcrrifF6tpNeSlpvCBe3Tl3GEDQEUxtI/Gxc30OUxlW";
		Assertions.assertTrue(this.PASSWORD_ENCODER.matches(rawPassword, defaultEncodedPassword));

		// 指定使用 bcrypt 加密算法的存储的密码
		String bcryptPassword = "{bcrypt}$2a$10$IRAHstZa7wgcrrifF6tpNeSlpvCBe3Tl3GEDQEUxtI/Gxc30OUxlW";
		Assertions.assertTrue(this.PASSWORD_ENCODER.matches(rawPassword, bcryptPassword));

		// 指定不用加密算法原样存储的密码
		String noopPassword = "{noop}a123456";
		Assertions.assertTrue(this.PASSWORD_ENCODER.matches(rawPassword, noopPassword));

		// 指定使用 MD5 存储的密码
		String md5Password = "{MD5}dc483e80a7a0bd9ef71d8cf973673924";
		Assertions.assertTrue(this.PASSWORD_ENCODER.matches(rawPassword, md5Password));
	}

}
