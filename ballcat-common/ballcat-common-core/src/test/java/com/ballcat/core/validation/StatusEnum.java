package com.ballcat.core.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author hccake
 */
@Getter
@RequiredArgsConstructor
public enum StatusEnum {

	SUCCESS(1), ERROR(0);

	private final int value;

	public static StatusEnum fromValue(int value) {
		switch (value) {
			case 1:
				return SUCCESS;
			case 0:
				return ERROR;
			default:
				return null;
		}
	}

}
