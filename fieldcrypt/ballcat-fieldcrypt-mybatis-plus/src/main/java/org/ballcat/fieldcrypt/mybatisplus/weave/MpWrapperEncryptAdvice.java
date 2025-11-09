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

package org.ballcat.fieldcrypt.mybatisplus.weave;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import org.ballcat.fieldcrypt.core.runtime.FieldCryptRuntimeConfig;
import org.ballcat.fieldcrypt.mybatisplus.encrypt.ArgEncryptor;

/**
 * MyBatis Plus 字段加密增强处理器
 * <p>
 * 使用 ByteBuddy Advice 拦截方法调用，在方法执行前对参数进行加密处理。 主要委托给 {@link ArgEncryptor} 进行具体的参数重写操作，
 * 实现对数据库操作中敏感字段的自动加密功能。
 *
 * @author Hccake
 * @since 2.0.0
 */
public final class MpWrapperEncryptAdvice {

	private MpWrapperEncryptAdvice() {
	}

	@Advice.OnMethodEnter
	public static void onEnter(@Advice.Origin("#t.#m") String where, @Advice.This(optional = true) Object self,
			@Advice.AllArguments(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object[] args,
			@Advice.Argument(value = 2, readOnly = false, typing = Assigner.Typing.DYNAMIC) Object valueArg) {
		try {
			Object rewritten = ArgEncryptor.tryRewriteLast(self, args);
			if (rewritten != null) {
				valueArg = rewritten; // decisive write-back to the real parameter slot
				if (args != null && args.length > 2) {
					args[2] = rewritten; // keep the snapshot array consistent
				}
			}
		}
		catch (Throwable t) {
			FieldCryptRuntimeConfig.Snapshot snap = MpWeaveRuntime.snap();
			boolean failFast = snap != null && snap.failFast;
			if (failFast) {
				throw new RuntimeException("MpWrapperEncryptAdvice failed at " + where + ": " + t.getMessage(), t);
			}
		}
	}

}
