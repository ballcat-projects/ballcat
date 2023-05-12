package com.hccake.starter.grpc.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2023-04-13 14:40
 */
@Getter
@AllArgsConstructor
public enum GrpcLogType {

	/**
	 * 发起
	 */
	SEND,
	/**
	 * 收到
	 */
	RECEIVE,

	;

}
