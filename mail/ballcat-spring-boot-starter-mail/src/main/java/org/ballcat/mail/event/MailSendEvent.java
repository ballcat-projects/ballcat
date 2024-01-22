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

package org.ballcat.mail.event;

import lombok.ToString;
import org.ballcat.mail.model.MailSendInfo;
import org.springframework.context.ApplicationEvent;

/**
 * @author Hccake 2020/2/27 18:00
 */
@ToString
public class MailSendEvent extends ApplicationEvent {

	public MailSendEvent(MailSendInfo mailSendInfo) {
		super(mailSendInfo);
	}

}
