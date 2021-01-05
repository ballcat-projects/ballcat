package com.hccake.ballcat.admin.modules.sys.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

/**
 * 字典修改事件
 *
 * @author Hccake 2021/1/5
 * @version 1.0
 */
@Getter
public class DictChangeEvent extends ApplicationEvent {

	private final String dictCode;

	public DictChangeEvent(String dictCode) {
		super(dictCode);
		this.dictCode = dictCode;
	}

}
