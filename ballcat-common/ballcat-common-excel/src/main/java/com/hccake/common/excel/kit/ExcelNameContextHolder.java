package com.hccake.common.excel.kit;

/**
 * @author lengleng
 * @date 2020/3/29
 */
public class ExcelNameContextHolder {


	private static final ThreadLocal<String> NAME_HOLDER = new InheritableThreadLocal<>();

	private ExcelNameContextHolder() {
	}


	public static String get() {
		String name = NAME_HOLDER.get();
		clear();
		return name;
	}

	public static void set(String name) {
		NAME_HOLDER.set(name);
	}

	/**
	 * 强制清空本地线程
	 * <p>
	 * 防止内存泄漏，如手动调用了push可调用此方法确保清除
	 * </p>
	 */
	public static void clear() {
		NAME_HOLDER.remove();
	}
}
