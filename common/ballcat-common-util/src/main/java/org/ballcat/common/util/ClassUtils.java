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

package org.ballcat.common.util;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author lingting 2021/2/25 21:17
 */
@SuppressWarnings({ "java:S3011", "unchecked" })
public final class ClassUtils {

	private ClassUtils() {
	}

	private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];

	private static final Map<String, Boolean> CACHE_CLASS_PRESENT = new ConcurrentHashMap<>(8);

	private static final Map<Class<?>, Field[]> CACHE_FIELDS = new ConcurrentHashMap<>(16);

	private static final Map<Class<?>, Method[]> CACHE_METHODS = new ConcurrentHashMap<>(16);

	private static final Map<Class<?>, Type[]> CACHE_TYPE_ARGUMENTS = new ConcurrentHashMap<>();

	private static final Map<Class<?>, Constructor<?>[]> CACHE_CONSTRUCTOR = new ConcurrentHashMap<>();

	/**
	 * 获取指定类的泛型
	 */
	public static Type[] typeArguments(Class<?> cls) {
		return CACHE_TYPE_ARGUMENTS.computeIfAbsent(cls, k -> {
			Type superclass = cls.getGenericSuperclass();

			if (!(superclass instanceof ParameterizedType)) {
				return new Type[0];
			}

			return ((ParameterizedType) superclass).getActualTypeArguments();
		});
	}

	public static List<Class<?>> classArguments(Class<?> cls) {
		Type[] types = typeArguments(cls);
		List<Class<?>> list = new ArrayList<>();

		for (Type type : types) {
			if (type instanceof Class<?>) {
				list.add((Class<?>) type);
			}
		}

		return list;
	}

	/**
	 * 确定class是否可以被加载
	 * @param className 完整类名
	 * @param classLoader 类加载
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		if (CACHE_CLASS_PRESENT.containsKey(className)) {
			return CACHE_CLASS_PRESENT.get(className);
		}
		try {
			Class.forName(className, true, classLoader);
			CACHE_CLASS_PRESENT.put(className, true);
			return true;
		}
		catch (Exception ex) {
			CACHE_CLASS_PRESENT.put(className, false);
			return false;
		}
	}

	public static <T> Set<Class<T>> scan(String basePack, Class<?> cls) throws IOException {
		return scan(basePack, tClass -> cls == null || cls.isAssignableFrom(tClass), (s, e) -> {
		});
	}

	/**
	 * 扫描指定包下, 所有继承指定类的class
	 * @param basePack 指定包 eg: live.lingting.component.item
	 * @param filter 过滤指定类
	 * @param error 获取类时发生异常处理
	 * @return java.util.Set<java.lang.Class < T>>
	 */
	@SuppressWarnings("java:S3776")
	public static <T> Set<Class<T>> scan(String basePack, Predicate<Class<T>> filter,
			BiConsumer<String, Exception> error) throws IOException {
		List<String> classNames = new ArrayList<>();
		String clsPath = basePack.replace(".", "/");
		URL url = Thread.currentThread().getContextClassLoader().getResource(clsPath);
		if (url == null) {
			return new HashSet<>();
		}
		if ("file".equals(url.getProtocol())) {
			String path = url.getFile();
			for (String file : FileUtils.scanFile(path, true)) {
				if (file.endsWith(".class")) {
					String className = basePack + "."
							+ file.substring(path.length(), file.length() - 6).replace(File.separator, ".");

					classNames.add(className);
				}
			}
		}
		else {
			URLConnection connection = url.openConnection();
			if (connection instanceof JarURLConnection) {
				JarURLConnection jarURLConnection = (JarURLConnection) connection;
				JarFile jarFile = jarURLConnection.getJarFile();

				Enumeration<JarEntry> entries = jarFile.entries();

				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();

					if (entryName.endsWith(".class") && entryName.startsWith(clsPath)) {
						classNames.add(entryName.substring(0, entryName.length() - 6).replace("/", "."));
					}
				}

			}
		}

		Set<Class<T>> classes = new HashSet<>();
		for (String className : classNames) {
			try {
				Class<T> aClass = (Class<T>) Class.forName(className);

				if (filter.test(aClass)) {
					classes.add(aClass);
				}
			}
			catch (Exception e) {
				error.accept(className, e);
			}
		}
		return classes;
	}

	/**
	 * 把指定对象的所有字段和对应的值组成Map
	 * @param o 需要转化的对象
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	public static Map<String, Object> toMap(Object o) {
		return toMap(o, field -> true, Field::getName, (field, v) -> v);
	}

	/**
	 * 把指定对象的所有字段和对应的值组成Map
	 * @param o 需要转化的对象
	 * @param filter 过滤不存入Map的字段, 返回false表示不存入Map
	 * @param toKey 设置存入Map的key
	 * @param toVal 自定义指定字段值的存入Map的数据
	 * @return java.util.Map<java.lang.String,java.lang.Object>
	 */
	public static <T> Map<String, T> toMap(Object o, Predicate<Field> filter, Function<Field, String> toKey,
			BiFunction<Field, Object, T> toVal) {
		if (o == null) {
			return Collections.emptyMap();
		}
		HashMap<String, T> map = new HashMap<>();
		for (Field field : fields(o.getClass())) {
			if (filter.test(field)) {
				Object val = null;

				try {
					val = field.get(o);
				}
				catch (IllegalAccessException e) {
					//
				}

				map.put(toKey.apply(field), toVal.apply(field, val));
			}
		}
		return map;
	}

	/**
	 * 获取所有字段, 并且设置为可读
	 * <p>
	 * 由于内部调用的了 {@link Field#setAccessible(boolean)}, 高版本使用会有问题
	 * </p>
	 * @param cls 指定类
	 * @return java.lang.reflect.Field[]
	 */
	public static Field[] fields(Class<?> cls) {
		return CACHE_FIELDS.computeIfAbsent(cls, k -> {
			List<Field> fields = new ArrayList<>();
			while (k != null && !k.isAssignableFrom(Object.class)) {
				for (Field field : k.getDeclaredFields()) {
					field.setAccessible(true);
					fields.add(field);
				}
				k = k.getSuperclass();
			}
			return fields.toArray(new Field[0]);
		});
	}

	public static Method[] methods(Class<?> cls) {
		return CACHE_METHODS.computeIfAbsent(cls, k -> {
			List<Method> methods = new ArrayList<>();
			while (k != null && !k.isAssignableFrom(Object.class)) {
				for (Method method : k.getDeclaredMethods()) {
					method.setAccessible(true);
					methods.add(method);
				}
				k = k.getSuperclass();
			}
			return methods.toArray(new Method[0]);
		});
	}

	public static Method method(Class<?> cls, String name) {
		return method(cls, name, EMPTY_CLASS_ARRAY);
	}

	public static Method method(Class<?> cls, String name, Class<?>... types) {
		return Arrays.stream(methods(cls)).filter(method -> {
			if (!Objects.equals(method.getName(), name)) {
				return false;
			}

			int len = types == null ? 0 : types.length;

			// 参数数量不一致
			if (len != method.getParameterCount()) {
				return false;
			}
			// 无参
			if (len == 0) {
				return true;
			}

			// 参数类型是否一致
			return Arrays.equals(types, method.getParameterTypes());
		}).findFirst().orElse(null);
	}

	public static Class<?> loadClass(String className) throws ClassNotFoundException {
		return loadClass(className, ClassUtils.class.getClassLoader());
	}

	public static Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException {
		return loadClass(className, classLoader, ClassLoader.getSystemClassLoader(), ClassUtils.class.getClassLoader(),
				Thread.currentThread().getContextClassLoader());
	}

	public static Class<?> loadClass(String className, ClassLoader... classLoaders) throws ClassNotFoundException {
		for (ClassLoader loader : classLoaders) {
			if (loader != null) {
				try {
					return loader.loadClass(className);
				}
				catch (ClassNotFoundException e) {
					//
				}
			}
		}
		throw new ClassNotFoundException(className + " not found");
	}

	/**
	 * 设置可访问对象的可访问权限为 true
	 * @param object 可访问的对象
	 * @param <T> 类型
	 * @return 返回设置后的对象
	 */
	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

	/**
	 * 方法名转字段名
	 * @param methodName 方法名
	 * @return java.lang.String 字段名
	 */
	public static String toFiledName(String methodName) {
		if (methodName.startsWith("is")) {
			methodName = methodName.substring(2);
		}
		else if (methodName.startsWith("get") || methodName.startsWith("set")) {
			methodName = methodName.substring(3);
		}

		if (methodName.length() == 1 || (methodName.length() > 1 && !Character.isUpperCase(methodName.charAt(1)))) {
			methodName = methodName.substring(0, 1).toLowerCase(Locale.ENGLISH) + methodName.substring(1);
		}

		return methodName;
	}

	@SuppressWarnings("unchecked")
	public static <T> Constructor<T>[] constructors(Class<T> cls) {
		return (Constructor<T>[]) CACHE_CONSTRUCTOR.computeIfAbsent(cls, Class::getConstructors);
	}

}
