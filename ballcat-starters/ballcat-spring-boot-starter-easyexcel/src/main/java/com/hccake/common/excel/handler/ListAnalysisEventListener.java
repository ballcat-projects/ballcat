package com.hccake.common.excel.handler;

import com.alibaba.excel.event.AnalysisEventListener;

import javax.validation.ConstraintViolation;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * list analysis EventListener
 *
 * @author L.cm
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	/**
	 * 获取 excel 解析的对象列表
	 * @return 集合
	 */
	public abstract List<T> getList();

	/**
	 * 获取异常校验结果
	 * @return 集合
	 */
	public abstract Map<Long, Set<ConstraintViolation<T>>> getErrors();

}
