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

package org.ballcat.security.core;

import org.springframework.lang.Nullable;

/**
 * 当前认证实体的属性访问器
 *
 * @author Hccake
 * @since 2.0.0
 */
public interface PrincipalAttributeAccessor {

	/**
	 * Get the principal attribute by name
	 * @param name the name of the attribute
	 * @param <A> the type of the attribute
	 * @return the attribute or {@code null} otherwise
	 */
	@Nullable
	<A> A getAttribute(String name);

	@Nullable
	<A> A getUserId();

	String getUsername();

}
