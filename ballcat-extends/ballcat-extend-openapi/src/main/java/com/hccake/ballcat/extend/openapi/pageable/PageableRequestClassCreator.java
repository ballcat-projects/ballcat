package com.hccake.ballcat.extend.openapi.pageable;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.asm.ClassReader;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.ClassWriter;
import org.springframework.asm.FieldVisitor;
import org.springframework.asm.MethodVisitor;
import org.springframework.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 分页请求参数对应的 class 创建器
 *
 * @author hccake
 */
public final class PageableRequestClassCreator {

	private PageableRequestClassCreator() {
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
				String s = CharSequenceUtil.upperFirst(key);
				String v = CharSequenceUtil.upperFirst(value);
				modifyMethodMap.put("get" + s, "get" + v);
				modifyMethodMap.put("set" + s, "set" + v);
			}
		}

		@Override
		public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
			if (modifyFiledMap.containsKey(name)) {
				return cv.visitField(access, modifyFiledMap.get(name), descriptor, signature, value);
			}
			return cv.visitField(access, name, descriptor, signature, value);
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			if (modifyMethodMap.containsKey(name)) {
				return cv.visitMethod(access, modifyMethodMap.get(name), desc, signature, exceptions);
			}
			return cv.visitMethod(access, name, desc, signature, exceptions);
		}

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

}
