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

package org.ballcat.springsecurity.configuration;

import org.ballcat.security.configuration.BallcatSecurityAutoConfiguration;
import org.ballcat.springsecurity.properties.SpringSecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Spring Security 的自动配置类
 *
 * @author Hccake
 * @since 2.0.0
 */
@EnableConfigurationProperties(SpringSecurityProperties.class)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@AutoConfiguration(before = BallcatSecurityAutoConfiguration.class)
@Import({ SpringSecurityComponentConfiguration.class, SpringSecurityWebSecurityConfiguration.class })
public class SpringSecurityAutoConfiguration {

}
