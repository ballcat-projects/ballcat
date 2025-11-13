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

import lombok.extern.slf4j.Slf4j;
import org.ballcat.fieldcrypt.core.exception.FieldCryptCryptoException;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.crypto.CryptoEngine;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 监听环境变量变化，并更新运行时配置。
 *
 * @author Hccake
 * @since 2.0.0
 */
@Slf4j
@Configuration
@ConditionalOnClass(name = "org.springframework.cloud.context.environment.EnvironmentChangeEvent")
public class FieldCryptCloudRefreshConfiguration {

	private static final String ENV_CHANGE_EVENT = "org.springframework.cloud.context.environment.EnvironmentChangeEvent";

	@Bean
	public ApplicationListener<ApplicationEvent> fieldCryptEnvironmentChangeListener(
			ConfigurableEnvironment environment, FieldCryptRuntimeConfig runtime, CryptoEngine registry) {
		return event -> {
			if (!ENV_CHANGE_EVENT.equals(event.getClass().getName())) {
				return;
			}
			FieldCryptProperties props = Binder.get(environment)
				.bind(FieldCryptProperties.PREFIX, Bindable.of(FieldCryptProperties.class))
				.orElseGet(FieldCryptProperties::new);
			String defaultAlgo = props.getDefaultAlgo();
			try {
				registry.setDefaultAlgo(defaultAlgo);
			}
			catch (FieldCryptCryptoException ex) {
				log.error("refresh field crypt config error, keep old snapshot.", ex);
				return; // invalid default, keep old snapshot
			}
			runtime.replace(new FieldCryptRuntimeConfig.Snapshot(props.isEnabled(), props.isEnableParameter(),
					props.isEnableResult(), props.isFailFast(), props.isRestorePlaintext(), defaultAlgo));
		};
	}

}
