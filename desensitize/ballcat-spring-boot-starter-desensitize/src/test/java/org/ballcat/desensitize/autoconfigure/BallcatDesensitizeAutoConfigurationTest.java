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

package org.ballcat.desensitize.autoconfigure;

import org.ballcat.desensitize.logging.logback.LoggerDesensitizationFilter;
import org.ballcat.desensitize.logging.logback.LoggerDesensitizationFilterHolder;
import org.ballcat.desensitize.logging.logback.TextDesensitizerHolder;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;

import static org.assertj.core.api.Assertions.assertThat;

class BallcatDesensitizeAutoConfigurationTest {

	private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
		.withConfiguration(AutoConfigurations.of(BallcatDesensitizeAutoConfiguration.class));

	@AfterEach
	void clearHolders() {
		TextDesensitizerHolder.set(null);
		LoggerDesensitizationFilterHolder.set(null);
	}

	@Test
	void shouldCreateSingletonCoreBeansWhenSpringCloudIsAbsent() {
		this.contextRunner.withClassLoader(new FilteredClassLoader("org.springframework.cloud.context")).run(ctx -> {
			assertThat(ctx).hasSingleBean(TextDesensitizer.class);
			assertThat(ctx).hasSingleBean(LoggerDesensitizationFilter.class);
			assertThat(ctx).hasSingleBean(TextDesensitizerHolderBinder.class);
			assertThat(ctx).hasSingleBean(LoggerDesensitizationFilterHolderBinder.class);
			assertThat(ctx).doesNotHaveBean("scopedTarget.desensitizationEngine");
			assertThat(ctx).doesNotHaveBean("scopedTarget.loggerDesensitizationFilter");
			assertThat(TextDesensitizerHolder.get()).isNotNull();
			assertThat(LoggerDesensitizationFilterHolder.get()).isNotNull();
		});
	}

	@Test
	void shouldCreateRefreshScopedCoreBeansAndRebindHoldersWhenSpringCloudIsPresent() {
		this.contextRunner.run(ctx -> {
			assertThat(ctx).hasSingleBean(TextDesensitizerHolderBinder.class);
			assertThat(ctx).hasSingleBean(LoggerDesensitizationFilterHolderBinder.class);
			assertThat(ctx.getBeanFactory().containsBeanDefinition("desensitizationEngine")).isTrue();
			assertThat(ctx.getBeanFactory().containsBeanDefinition("loggerDesensitizationFilter")).isTrue();
			assertThat(ctx.getBeanFactory().containsBeanDefinition("scopedTarget.desensitizationEngine")).isTrue();
			assertThat(ctx.getBeanFactory().containsBeanDefinition("scopedTarget.loggerDesensitizationFilter"))
				.isTrue();
			assertThat(ctx.getBeanFactory().getBeanDefinition("scopedTarget.desensitizationEngine").getScope())
				.isEqualTo("refresh");
			assertThat(ctx.getBeanFactory().getBeanDefinition("scopedTarget.loggerDesensitizationFilter").getScope())
				.isEqualTo("refresh");

			TextDesensitizerHolder.set(null);
			LoggerDesensitizationFilterHolder.set(null);
			ctx.publishEvent(new RefreshScopeRefreshedEvent());

			assertThat(TextDesensitizerHolder.get()).isNotNull();
			assertThat(LoggerDesensitizationFilterHolder.get()).isNotNull();
		});
	}

}
