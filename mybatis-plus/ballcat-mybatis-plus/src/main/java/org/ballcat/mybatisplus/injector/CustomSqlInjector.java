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

package org.ballcat.mybatisplus.injector;

import java.util.List;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import lombok.RequiredArgsConstructor;

/**
 * 默认的注入器，提供属性来注入自定义方法
 *
 * @author lingting 2020/5/27 11:46
 */
@RequiredArgsConstructor
public class CustomSqlInjector extends DefaultSqlInjector {

	private final List<AbstractMethod> methods;

	@Override
	public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
		List<AbstractMethod> list = super.getMethodList(mapperClass, tableInfo);
		list.addAll(this.methods);
		return list;
	}

}
