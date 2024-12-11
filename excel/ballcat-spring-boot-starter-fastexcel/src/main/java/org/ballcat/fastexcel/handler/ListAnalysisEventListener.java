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

package org.ballcat.fastexcel.handler;

import java.util.List;

import javax.validation.Validation;
import javax.validation.Validator;

import cn.idev.excel.event.AnalysisEventListener;
import lombok.Getter;
import lombok.Setter;
import org.ballcat.fastexcel.domain.ErrorMessage;

/**
 * list analysis EventListener
 *
 * @author L.cm
 */
@Getter
@Setter
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

	/**
	 * 校验器
	 */
	private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	/**
	 * 获取 excel 解析的对象列表
	 * @return 集合
	 */
	public abstract List<T> getList();

	/**
	 * 获取异常校验结果
	 * @return 集合
	 */
	public abstract List<ErrorMessage> getErrors();

}
