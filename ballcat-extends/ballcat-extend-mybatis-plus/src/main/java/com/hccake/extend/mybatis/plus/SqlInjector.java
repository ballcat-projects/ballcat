package com.hccake.extend.mybatis.plus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * @author lingting 2020/5/27 11:46
 */
@RequiredArgsConstructor
public class SqlInjector extends DefaultSqlInjector {

	private final List<AbstractMethod> list;

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass) {
		List<AbstractMethod> list = super.getMethodList(mapperClass);
		list.addAll(this.list);
		return list;
	}

}
