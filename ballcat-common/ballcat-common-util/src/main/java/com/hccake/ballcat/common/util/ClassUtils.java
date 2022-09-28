package com.hccake.ballcat.common.util;

import cn.hutool.core.util.ClassUtil;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author lingting 2021/2/25 21:17
 */
public class ClassUtils extends ClassUtil {

	private static final Map<String, Boolean> CACHE = new ConcurrentHashMap<>(8);

	private static final Map<Class<?>, Field[]> CACHE_FIELDS = new ConcurrentHashMap<>(16);

	/**
	 * 确定class是否可以被加载
	 * @param className 完整类名
	 * @param classLoader 类加载
	 */
	public static boolean isPresent(String className, ClassLoader classLoader) {
		if (CACHE.containsKey(className)) {
			return CACHE.get(className);
		}
		try {
			Class.forName(className, true, classLoader);
			CACHE.put(className, true);
			return true;
		}
		catch (Exception ex) {
			CACHE.put(className, false);
			return false;
		}
	}

	public static <T> Set<Class<T>> scan(String basePack, Class<?> cls) throws IOException {
		return scan(basePack, tClass -> cls == null || cls.isAssignableFrom(tClass), (s, e) -> {
		});
	}

	/**
	 * 扫描指定包下, 所有继承指定类的class
	 * @param basePack 指定包 eg: live.lingting.wirelesstools.item
	 * @param filter 过滤指定类
	 * @param error 获取类时发生异常处理
	 * @return java.util.Set<java.lang.Class < T>>
	 */
	public static <T> Set<Class<T>> scan(String basePack, Function<Class<T>, Boolean> filter,
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

				if (filter.apply(aClass)) {
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
	public static <T> Map<String, T> toMap(Object o, Function<Field, Boolean> filter, Function<Field, String> toKey,
			BiFunction<Field, Object, T> toVal) {
		if (o == null) {
			return Collections.emptyMap();
		}
		HashMap<String, T> map = new HashMap<>();
		for (Field field : fields(o.getClass())) {
			if (filter.apply(field)) {
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
	 * 获取指定类及其父类的所有字段, 并且设置成可访问.
	 * @param cls class
	 * @return java.lang.reflect.Field[]
	 */
	@SuppressWarnings("java:S3011")
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

}
