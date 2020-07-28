package com.hccake.ballcat.admin.modules.lov.enums;

import com.baomidou.mybatisplus.core.enums.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2020/7/6 10:04
 */
@Getter
@AllArgsConstructor
public enum Tag implements IEnum<String> {

	/**
	 * 标签类型
	 */
	INPUT_TEXT, INPUT_NUMBER, SELECT,;

	@Override
	public String getValue() {
		return toString();
	}

	@Override
	public String toString() {
		return name().toLowerCase().replace("_", "-");
	}

	public static void main(String[] args) {
		System.out.println(Tag.INPUT_NUMBER);
	}

}
