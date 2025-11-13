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
import org.ballcat.fieldcrypt.mybatisplus.weave.MpWrapperEncryptWeaver;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * 在Spring Boot生命周期中尽早安装MP织入器，以确保目标类在加载前被织入。
 *
 * @author Hccake
 */
@Slf4j
public class MpWeaverContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	private static volatile boolean installed = false;

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		if (installed) {
			log.info("[MP-Weave] initializer already installed");
			return;
		}
		try {
			new MpWrapperEncryptWeaver().install();
			installed = true;
			log.info("[MP-Weave] initializer install success");
		}
		catch (Throwable t) {
			log.error("[MP-Weave] initializer install failed", t);
		}
	}

}
