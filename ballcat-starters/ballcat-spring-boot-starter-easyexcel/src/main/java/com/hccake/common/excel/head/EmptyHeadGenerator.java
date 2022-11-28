package com.hccake.common.excel.head;

/**
 * 空的 excel 头生成器，用来忽略 excel 头生成
 *
 * @author Hccake
 */
public class EmptyHeadGenerator implements HeadGenerator {

	@Override
	public HeadMeta head(Class<?> clazz) {
		return new HeadMeta();
	}

}
