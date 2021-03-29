package com.hccake.extend.pay.viratual;

/**
 * 线程处理对象需要实现此类
 *
 * @author lingting 2021/1/5 11:15
 */
public interface VerifyObj {

	/**
	 * 获取当前订单的交易hash
	 * @return java.lang.String
	 * @author lingting 2021-01-05 11:15
	 */
	String getHash();

}
