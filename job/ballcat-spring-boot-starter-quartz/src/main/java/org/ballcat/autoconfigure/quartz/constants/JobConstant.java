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

package org.ballcat.autoconfigure.quartz.constants;

/**
 * Quartz 常量
 *
 * @author evil0th Create on 2024/5/13
 */
public final class JobConstant {

	private JobConstant() {
	}

	/**
	 * 保留Job分组
	 */
	public static final String RESERVED_JOB_GROUP = "ReservedJobGroup";

	public static final String QUARTZ_PROPERTIES_PREFIX = "spring.quartz.properties";

	/**
	 * quartz插件配置前缀
	 */
	public static final String PLUGIN_PROPERTIES_PREFIX = "org.quartz.plugin";

	public static final String DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME = "storeJobHistory";

	public static final String DEFAULT_STORE_JOB_HISTORY_PLUGIN_PROPERTIES_PREFIX = QUARTZ_PROPERTIES_PREFIX + "."
			+ PLUGIN_PROPERTIES_PREFIX + "." + DEFAULT_STORE_JOB_HISTORY_PLUGIN_NAME;

	public static final String DEFAULT_STORE_JOB_HISTORY_PLUGIN_CLASS = "org.ballcat.autoconfigure.quartz.plugin.StoreJobHistoryPlugin";

}
