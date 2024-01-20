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

package org.ballcat.common.core.exception;

import org.ballcat.common.model.result.SystemResultCode;

/**
 * sql防注入校验异常
 *
 * @author Hccake 2019/10/19 16:52
 */
public class SqlCheckedException extends BusinessException {

	public SqlCheckedException(SystemResultCode systemResultMsg) {
		super(systemResultMsg);
	}

	public SqlCheckedException(SystemResultCode systemResultMsg, Throwable e) {
		super(systemResultMsg, e);
	}

	public SqlCheckedException(int code, String msg) {
		super(code, msg);
	}

	public SqlCheckedException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

}
