package com.hccake.ballcat.admin.modules.lov.enums;

import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2020/7/6 10:04
 */
@Getter
@AllArgsConstructor
public enum Tag {

	/**
	 * 标签类型
	 */
	INPUT_TEXT, INPUT_NUMBER, SELECT, DICT_SELECT;

}
