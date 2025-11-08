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

package org.ballcat.fieldcrypt.mybatis.testutil;

/**
 * 简化构建 SqlSessionFactory 的测试配置.
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class TestConfig {

	public final boolean useActualParamName;

	public final boolean restorePlaintext;

	public final boolean failFast;

	public final boolean lazyLoadingEnabled;

	public final boolean usePagingLikeInterceptor;

	public TestConfig(boolean useActualParamName, boolean restorePlaintext, boolean failFast,
			boolean lazyLoadingEnabled, boolean usePagingLikeInterceptor) {
		this.useActualParamName = useActualParamName;
		this.restorePlaintext = restorePlaintext;
		this.failFast = failFast;
		this.lazyLoadingEnabled = lazyLoadingEnabled;
		this.usePagingLikeInterceptor = usePagingLikeInterceptor;
	}

	public static TestConfig defaults() {
		return new TestConfig(true, false, true, false, false);
	}

	public static TestConfig withRestore(boolean restore) {
		return new TestConfig(true, restore, true, false, false);
	}

	public static TestConfig withActualParam(boolean actual) {
		return new TestConfig(actual, false, true, false, false);
	}

	public static TestConfig withFailFast(boolean ff) {
		return new TestConfig(true, false, ff, false, false);
	}

	public static TestConfig withLazy(boolean lazy) {
		return new TestConfig(true, false, true, lazy, false);
	}

	public static TestConfig withPagingLike(boolean usePaging) {
		return new TestConfig(true, false, true, false, usePaging);
	}

}
