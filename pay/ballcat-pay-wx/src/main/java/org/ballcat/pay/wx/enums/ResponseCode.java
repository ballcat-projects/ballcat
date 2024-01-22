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

package org.ballcat.pay.wx.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回code
 *
 * @author lingting 2021/2/1 11:31
 */
@Getter
@AllArgsConstructor
public enum ResponseCode {

	/**
	 * 成功
	 */
	SUCCESS,
	/**
	 * 失败
	 */
	FAIL,
	/**
	 * 异常
	 */
	ERROR;

	@JsonCreator
	public static ResponseCode of(String status) {
		switch (status) {
			case "SUCCESS":
				return SUCCESS;
			case "FAIL":
				return FAIL;
			default:
				return ERROR;
		}
	}

}
