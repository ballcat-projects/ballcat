package com.hccake.ballcat.system.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * lov 修改事件
 *
 * @author lingting 2021/3/25 17:18
 */
@Getter
@ToString
public class LovChangeEvent extends ApplicationEvent {

	private final String keyword;

	public LovChangeEvent(String keyword) {
		super(keyword);
		this.keyword = keyword;
	}

	public static LovChangeEvent of(String keyword) {
		return new LovChangeEvent(keyword);
	}

}
