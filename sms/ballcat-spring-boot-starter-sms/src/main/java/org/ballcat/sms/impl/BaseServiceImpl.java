/*
 * Copyright 2023 the original author or authors.
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
package org.ballcat.sms.impl;

import cn.hutool.core.util.StrUtil;
import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2020/4/26 13:45
 */
@SuppressWarnings("java:S1610")
@Slf4j
public abstract class BaseServiceImpl {

	/**
	 * b 异常返回处理
	 */
	public SmsSenderResult errRet(TypeEnum platform, Set<String> phoneNumbers, String msg, Exception e) {
		String id = StrUtil.uuid();
		log.error(msg + "\nerror_id: " + id, e);
		return SmsSenderResult.generateException(platform, phoneNumbers, id, e);
	}

}
