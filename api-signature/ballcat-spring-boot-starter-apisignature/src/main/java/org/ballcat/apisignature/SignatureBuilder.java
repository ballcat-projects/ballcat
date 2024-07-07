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

package org.ballcat.apisignature;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 签名构建器.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
@Accessors(chain = true)
@Setter
public class SignatureBuilder {

	/**
	 * 请求方法。
	 */
	private String httpMethod;

	/**
	 * 请求URI。
	 */
	private String requestURI;

	/**
	 * 查询参数。
	 */
	private String queryString;

	/**
	 * 请求荷载。
	 */
	private String requestPayload;

	/**
	 * 请求时间戳。
	 */
	private String timestamp;

	/**
	 * 随机值。
	 */
	private String nonce;

	/**
	 * 访问Key。
	 */
	private String accessKey;

	/**
	 * 访问秘钥。
	 */
	private String secretKey;

	private static final String DELIMITER = "#";

	public String build() {
		Assert.notNull(this.httpMethod, "Http Method 不能为 null");
		Assert.notNull(this.requestURI, "Request URI 不能为 null");
		Assert.notNull(this.timestamp, "timestamp 不能为 null");
		Assert.notNull(this.nonce, "nonce 不能为 null");
		Assert.notNull(this.accessKey, "Access Key 不能为 null");
		Assert.notNull(this.secretKey, "Secret Key 不能为 null");

		StringBuilder builder = new StringBuilder();

		builder.append(this.httpMethod).append(DELIMITER);

		// 拼接不包含域名的完整请求 url，需要包含 queryString
		if (StringUtils.isNotBlank(this.queryString)) {
			builder.append(this.requestURI).append("?").append(this.queryString).append(DELIMITER);
		}
		else {
			builder.append(this.requestURI).append(DELIMITER);
		}

		// 拼接请求体
		if (StringUtils.isNotBlank(this.requestPayload)) {
			builder.append(this.requestPayload).append(DELIMITER);
		}

		// 请求头参数拼接
		builder.append(this.timestamp)
			.append(DELIMITER)
			.append(this.nonce)
			.append(DELIMITER)
			.append(this.accessKey)
			.append(DELIMITER);

		// 最后拼接上 secretKey
		builder.append(this.secretKey);
		String rawSignatureStr = builder.toString();

		// MD5 摘要计算
		String signature = generateMD5(rawSignatureStr);
		log.debug("raw signature str：{}, md5 signature: {}", rawSignatureStr, signature);

		return signature;
	}

	public static String generateMD5(String input) {
		try {
			// 获取 MD5 摘要算法实例
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算摘要
			byte[] messageDigest = md.digest(input.getBytes());
			// 将摘要转换为十六进制字符串
			BigInteger no = new BigInteger(1, messageDigest);
			StringBuilder hashText = new StringBuilder(no.toString(16));
			// 补齐前导零
			while (hashText.length() < 32) {
				hashText.insert(0, "0");
			}
			return hashText.toString();
		}
		catch (NoSuchAlgorithmException e) {
			throw new ApiSignatureException("MD5 algorithm not found", e);
		}
	}

}
