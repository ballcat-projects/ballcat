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

/**
 * API Key 管理器。 用于根据 Access Key 获取用户主体信息，以及根据用户主体获取对应的 secretKey。
 *
 * @author Hccake
 * @since 2.0.0
 */
public interface ApiKeyManager {

	/**
	 * 根据传入的 Access Key 获取用户主体信息
	 * @param accessKey Access Key
	 * @return subject
	 */
	Object getSubject(String accessKey);

	/**
	 * 根据用户主体获取到对应的 secretKey.
	 * @param subject 用户主体
	 * @return 如果找到对应的 secretKey，则返回该 secretKey；否则返回 null
	 */
	String getSecretKey(Object subject);

}
