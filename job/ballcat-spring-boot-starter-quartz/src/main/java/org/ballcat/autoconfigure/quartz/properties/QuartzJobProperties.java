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

package org.ballcat.autoconfigure.quartz.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author evil0th Create on 2024/5/8
 */
@Getter
@Setter
@ConfigurationProperties(QuartzJobProperties.PREFIX)
public class QuartzJobProperties {

	public static final String PREFIX = "ballcat.quartz.job";

	/**
	 * 是否启用Quartz调度任务，默认：开启
	 */
	private boolean enabled = true;

}
