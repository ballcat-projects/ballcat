package com.hccake.sample.pay.virtual.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 交易校验返回结果
 *
 * @author lingting 2021/1/5 14:38
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Result {

	/**
	 * 如果校验失败, 这里的值为错误信息
	 */
	private String message;

}
