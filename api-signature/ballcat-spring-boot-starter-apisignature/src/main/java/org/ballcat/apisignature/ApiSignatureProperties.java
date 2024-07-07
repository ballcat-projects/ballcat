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

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * API 签名配置属性
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@ConfigurationProperties(prefix = "wd.api-signature")
public class ApiSignatureProperties {

	/**
	 * 需要进行签名校验的 url 的规则列表。
	 */
	private List<String> includeUrlPattens = Collections.singletonList("/**");

	/**
	 * 不需要进行签名校验的 url 规则列表，优先级在 includeUrlPatten 之上。 eg. 例如 includeUrlPatten 和
	 * excludeUrlPatten 中同时包含 /abc，则访问 /abc 时不会进行签名校验。
	 */
	private List<String> excludeUrlPattens = Collections.emptyList();

	/**
	 * 请求 uri 的前缀字符串，当服务在网关或者 nginx 后面时，用户实际请求的地址可能会带有前缀，在经过转发时前缀被 rewrite 掉了。
	 * 这时需要配置此属性，以便在构建签名对比时恢复完整的 uri。
	 */
	private String uriPrefix;

	/**
	 * 签名信息的请求头。
	 */
	private String signatureHeader = "X-Signature";

	/**
	 * 存放请求时间戳的请求头。
	 */
	private String timestampHeader = "X-Timestamp";

	/**
	 * 存放请求32位随机字符串的请求头。
	 */
	private String nonceHeader = "X-Nonce";

	/**
	 * 存放当前请求方标识的请求头。
	 */
	private String accessKeyHeader = "X-Access-Key";

	/**
	 * 请求时间戳和服务器时间戳相差绝对值的范围，单位毫秒。
	 */
	private long timestampDiffThreshold = 5 * 60 * 1000L;

	/**
	 * nonce 随机字符串的存储过期时长。建议大于时间戳的有效区间。
	 */
	private long nonceTimeout = 15 * 60 * 1000L;

	/**
	 * nonce 随机字符串的存储过期时长的单位，默认毫秒。
	 */
	private TimeUnit nonceTimeoutUnit = TimeUnit.MILLISECONDS;

}
