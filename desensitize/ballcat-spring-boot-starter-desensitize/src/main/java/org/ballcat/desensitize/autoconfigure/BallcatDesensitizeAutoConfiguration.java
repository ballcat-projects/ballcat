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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import org.ballcat.desensitize.logging.logback.LoggerDesensitizationFilter;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.BoundaryOptions;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动配置：构建核心引擎并注入 Logback EngineHolder。
 *
 * @author Hccake
 */
@Configuration
@EnableConfigurationProperties({ BallcatDesensitizeTextProperties.class, BallcatDesensitizeLoggingProperties.class })
@ConditionalOnClass({ LoggerContext.class, PatternLayout.class })
public class BallcatDesensitizeAutoConfiguration {

	@RefreshScope
	@Bean
	@ConditionalOnMissingBean
	public TextDesensitizer desensitizationEngine(BallcatDesensitizeTextProperties props) {
		BoundaryOptions matcher = props.getBoundary();
		List<RuleSpec> ruleSpecs = props.getRules();
		return new TextDesensitizerBuilder(ruleSpecs).config(matcher).build();
	}

	/**
	 * 通过代理 Bean 绑定 EngineHolder，并在刷新后重新绑定。
	 */
	@Bean
	public TextDesensitizerHolderBinder engineHolderBinder(ObjectProvider<TextDesensitizer> engineProvider) {
		return new TextDesensitizerHolderBinder(engineProvider);
	}

	/**
	 * 基于配置构建 Logger 过滤器，并注入 Holder；支持刷新后更新。
	 */
	@RefreshScope
	@Bean
	public LoggerDesensitizationFilter loggerDesensitizationFilter(BallcatDesensitizeLoggingProperties props) {
		boolean enabled = props.isEnabled();
		Map<String, Boolean> rules = new HashMap<>(props.getScopes());
		// 支持 root 作为默认决策；未配置时默认 true（与此前行为一致）
		boolean defaultDecision = rules.getOrDefault("root", Boolean.TRUE);
		rules.remove("root");
		return new LoggerDesensitizationFilter(enabled, defaultDecision, rules);
	}

	@Bean
	public LoggerDesensitizationFilterHolderBinder loggerDesensitizationFilterHolderBinder(
			org.springframework.beans.factory.ObjectProvider<LoggerDesensitizationFilter> provider) {
		return new LoggerDesensitizationFilterHolderBinder(provider);
	}

}
