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

import java.lang.instrument.Instrumentation;
import java.util.concurrent.atomic.AtomicBoolean;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * MyBatis-Plus 条件构造器加密织入器
 * <p>
 * 该类负责使用 ByteBuddy 对 MyBatis-Plus 的条件构造器相关方法进行字节码增强， 实现在查询和更新操作中对敏感字段的自动加解密处理。
 * <p>
 * 主要职责：
 * <ul>
 * <li>MpWrapperEncryptWeaver：安装 Java Agent 并对目标类的方法织入加密增强逻辑</li>
 * <li>MpWrapperEncryptAdvice：实际的加密增强逻辑，对匹配的方法调用进行拦截和处理</li>
 * </ul>
 *
 * @author Hccake
 * @since 2.0.0
 */
public class MpWrapperEncryptWeaver {

	private static final AtomicBoolean INSTALLED = new AtomicBoolean(false);

	public void install() {
		if (!INSTALLED.compareAndSet(false, true)) {
			return;
		}
		// Install agent to obtain Instrumentation
		Instrumentation inst = ByteBuddyAgent.install();
		System.out.println("[MP-Weave] inst: retransformSupported=" + inst.isRetransformClassesSupported()
				+ ", redefineSupported=" + inst.isRedefineClassesSupported());

		// Immediately redefine AbstractWrapper: only boolean-leading overloads of
		// condition methods
		new ByteBuddy().redefine(AbstractWrapper.class)
			.visit(Advice.to(MpWrapperEncryptAdvice.class)
				.on(ElementMatchers.named("eq")
					.or(ElementMatchers.named("ne"))
					.or(ElementMatchers.named("in"))
					.or(ElementMatchers.named("notIn"))
					.and(ElementMatchers.takesArgument(0, boolean.class))))
			.make()
			.load(AbstractWrapper.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

		// Update wrappers: weave set(boolean, R, Object) in concrete classes, not the
		// interface
		new ByteBuddy().redefine(UpdateWrapper.class)
			.visit(Advice.to(MpWrapperEncryptAdvice.class)
				.on(ElementMatchers.named("set").and(ElementMatchers.takesArgument(0, boolean.class))))
			.make()
			.load(UpdateWrapper.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());

		new ByteBuddy().redefine(LambdaUpdateWrapper.class)
			.visit(Advice.to(MpWrapperEncryptAdvice.class)
				.on(ElementMatchers.named("set").and(ElementMatchers.takesArgument(0, boolean.class))))
			.make()
			.load(LambdaUpdateWrapper.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
		System.out.println("[MP-Weave] ByteBuddy weaver installed (encryption advice wired)");
	}

}
