package com.hccake.ballcat.extend.openapi.pageable;

/**
 * 通过内存的字节数组直接加载 class
 *
 * @author hccake
 */
public class ByteClassLoader extends ClassLoader {

	public ByteClassLoader(ClassLoader contextClassLoader) {
		super(contextClassLoader);
	}

	public Class<?> defineClass(byte[] bytes) {
		return defineClass(null, bytes, 0, bytes.length);
	}

}
