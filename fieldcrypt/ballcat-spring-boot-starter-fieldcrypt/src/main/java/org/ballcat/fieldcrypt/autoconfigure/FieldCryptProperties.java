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

package org.ballcat.fieldcrypt.autoconfigure;

import lombok.Data;
import org.ballcat.fieldcrypt.crypto.impl.AesCbcFixedIvCryptoAlgorithm;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * FieldCrypt 组件配置属性.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@ConfigurationProperties(prefix = FieldCryptProperties.PREFIX)
public class FieldCryptProperties {

	public static final String PREFIX = "ballcat.fieldcrypt";

	/** 是否启用组件 */
	private boolean enabled = true;

	/** 是否启用参数加密拦截器 */
	private boolean enableParameter = true;

	/** 是否启用结果解密拦截器 */
	private boolean enableResult = true;

	/** 加解密失败或配置不当是否快速失败抛出异常 */
	private boolean failFast = true;

	/** 是否在执行后恢复参数明文 */
	private boolean restorePlaintext = true;

	/** 默认算法标识（需与注册的算法名称一致） */
	private String defaultAlgo = AesCbcFixedIvCryptoAlgorithm.ALGO_NAME;

	/**
	 * AES256密钥。
	 */
	private String aesKey = "";

	public void setDefaultAlgo(String defaultAlgo) {
		this.defaultAlgo = defaultAlgo == null ? null : defaultAlgo.trim();
	}

}
