package com.hccake.extend.mybatis.plus.injector;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 默认的注入器，提供属性来注入自定义方法
 *
 * @author lingting 2020/5/27 11:46
 */
@RequiredArgsConstructor
public class CustomSqlInjector extends DefaultSqlInjector {

	private final List<AbstractMethod> list;

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		List<AbstractMethod> list = super.getMethodList(mapperClass);
		list.addAll(this.list);
		return list;
	}

}
