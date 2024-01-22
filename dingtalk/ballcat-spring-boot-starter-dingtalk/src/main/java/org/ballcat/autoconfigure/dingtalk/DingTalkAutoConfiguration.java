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

package org.ballcat.autoconfigure.dingtalk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.dingtalk.DingTalkSender;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2020/6/11 23:14
 */
@Slf4j
@RequiredArgsConstructor
@AutoConfiguration
@ConditionalOnProperty(name = "ballcat.dingtalk.url")
@EnableConfigurationProperties({ DingTalkProperties.class })
public class DingTalkAutoConfiguration {

	private final DingTalkProperties dingTalkProperties;

	@Bean
	@ConditionalOnMissingBean
	public DingTalkSender dingTalkSender() {
		return new DingTalkSender(this.dingTalkProperties.getUrl()).setSecret(this.dingTalkProperties.getSecret());
	}

}
