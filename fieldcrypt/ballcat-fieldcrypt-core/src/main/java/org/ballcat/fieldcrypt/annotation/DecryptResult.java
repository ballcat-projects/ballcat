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

package org.ballcat.fieldcrypt.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.ballcat.fieldcrypt.crypto.CryptoAlgorithmRegistry;

/**
 * 方法结果解密标记：用于指示该 Mapper 方法的返回结果需要按指定算法解密。 支持 String、List<String>、String[] 顶层返回形态。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface DecryptResult {

	/**
	 * 算法标识，在 {@link CryptoAlgorithmRegistry} 注册表中查找，如 "AES_GCM"，为空时使用全局默认。
	 * @return 算法标识
	 */
	String algo() default "";

	/**
	 * 透传给具体算法实现的自由参数字符串（如密钥ID等）。
	 * @return 参数字符串
	 */
	String params() default "";

}
