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

package org.ballcat.fieldcrypt.mybatis.testmodel;

import lombok.Data;
import org.ballcat.fieldcrypt.annotation.Encrypted;
import org.ballcat.fieldcrypt.annotation.EncryptedEntity;

/**
 * 测试用户实体.
 *
 * @author Hccake
 * @since 2.0.0
 */
@Data
@EncryptedEntity
public class User {

	private Long id;

	private String name;

	@Encrypted
	private String mobile; // 需要加密

	@Encrypted
	private String email; // 第二个需要加密的字段

	private String address; // 不需要加密的字段

}
