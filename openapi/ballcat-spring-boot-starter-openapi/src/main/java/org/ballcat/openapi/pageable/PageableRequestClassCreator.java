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

package org.ballcat.openapi.pageable;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.util.StringUtils;

/**
 * 分页请求参数对应的 class 创建器
 *
 * @author hccake
 */
public final class PageableRequestClassCreator {

	private PageableRequestClassCreator() {
	}

	public static Class<?> create(Map<String, String> modifyFiledMap) throws IOException {
		String className = PageableRequest.class.getCanonicalName();
		String classFilePath = className.replace('.', '/') + ".class";
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();

		try (InputStream resourceAsStream = contextClassLoader.getResourceAsStream(classFilePath)) {
			Objects.requireNonNull(resourceAsStream, className + " 必须存在");
			ClassReader classReader = new ClassReader(resourceAsStream);
			ClassWriter classWriter = new ClassWriter(classReader, 0);
			ModifyFieldNameAdapter modifyFieldNameAdapter = new ModifyFieldNameAdapter(Opcodes.ASM9, classWriter,
					modifyFiledMap);
			classReader.accept(modifyFieldNameAdapter, 0);

			ByteClassLoader myClassLoader = new ByteClassLoader(contextClassLoader);
			return myClassLoader.defineClass(classWriter.toByteArray());
		}
	}

	public static class ModifyFieldNameAdapter extends ClassVisitor {

		private final Map<String, String> modifyFiledMap;

		private final Map<String, String> modifyMethodMap;

		protected ModifyFieldNameAdapter(int api, ClassVisitor classVisitor, Map<String, String> modifyFiledMap) {
			super(api, classVisitor);
			this.modifyFiledMap = modifyFiledMap;
			this.modifyMethodMap = new HashMap<>();
			for (Map.Entry<String, String> entry : modifyFiledMap.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				String s = StringUtils.capitalize(key);
				String v = StringUtils.capitalize(value);
				this.modifyMethodMap.put("get" + s, "get" + v);
				this.modifyMethodMap.put("set" + s, "set" + v);
			}
		}

		@Override
		public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
			if (this.modifyFiledMap.containsKey(name)) {
				return this.cv.visitField(access, this.modifyFiledMap.get(name), descriptor, signature, value);
			}
			return this.cv.visitField(access, name, descriptor, signature, value);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (this.modifyMethodMap.containsKey(name)) {
				return this.cv.visitMethod(access, this.modifyMethodMap.get(name), desc, signature, exceptions);
			}
			return this.cv.visitMethod(access, name, desc, signature, exceptions);
		}

	}

}
