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

package org.ballcat.sms.sender;

import java.util.Set;

import org.ballcat.sms.SmsSenderResult;
import org.ballcat.sms.enums.TypeEnum;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

/**
 * 短信发送 接口类
 *
 * @author lingting 2020/4/26 9:55
 */
@SuppressWarnings("java:S112")
public abstract class AbstractSmsSender<P extends AbstractSenderParams<P>> {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	/**
	 * 发送短信
	 * @param p 参数配置
	 * @return 短信发送结果
	 */
	public SmsSenderResult send(P p) {
		TypeEnum platform = platform();
		try {
			return doSend(p);
		}
		catch (Exception e) {
			String msg = platform.name() + "平台发送短信出现异常!";
			return errRet(platform, p.getPhoneNumbers(), msg, e);
		}
	}

	/**
	 * 异常返回处理
	 */
	public SmsSenderResult errRet(TypeEnum platform, Set<String> phoneNumbers, String msg, Exception e) {
		String id = ObjectId.get().toString();
		this.log.error("sms result error! errorId: {}; {}", id, msg, e);
		return SmsSenderResult.generateException(platform, phoneNumbers, id, e);
	}

	public abstract TypeEnum platform();

	protected abstract SmsSenderResult doSend(P p) throws Exception;

}
