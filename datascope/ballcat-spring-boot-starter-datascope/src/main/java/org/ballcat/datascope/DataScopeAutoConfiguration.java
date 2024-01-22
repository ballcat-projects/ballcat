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

package org.ballcat.datascope;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.ballcat.datascope.handler.DataPermissionHandler;
import org.ballcat.datascope.handler.DefaultDataPermissionHandler;
import org.ballcat.datascope.interceptor.DataPermissionAnnotationAdvisor;
import org.ballcat.datascope.interceptor.DataPermissionInterceptor;
import org.ballcat.datascope.processor.DataScopeSqlProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * @author hccake
 */
@AutoConfiguration
@RequiredArgsConstructor
@ConditionalOnBean(DataScope.class)
public class DataScopeAutoConfiguration {

	/**
	 * 数据权限处理器
	 * @param dataScopeList 需要控制的数据范围集合
	 * @return DataPermissionHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionHandler dataPermissionHandler(List<DataScope> dataScopeList) {
		return new DefaultDataPermissionHandler(dataScopeList);
	}

	/**
	 * 数据权限注解 Advisor，用于处理数据权限的链式调用关系
	 * @return DataPermissionAnnotationAdvisor
	 */
	@Bean
	@ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

	/**
	 * mybatis 拦截器，用于拦截处理 sql
	 * @param dataPermissionHandler 数据权限处理器
	 * @return DataPermissionInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean
	public DataPermissionInterceptor dataPermissionInterceptor(DataPermissionHandler dataPermissionHandler) {
		return new DataPermissionInterceptor(new DataScopeSqlProcessor(), dataPermissionHandler);
	}

}
