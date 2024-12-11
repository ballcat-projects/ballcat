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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.read.metadata.holder.ReadRowHolder;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.fastexcel.domain.ErrorMessage;

/**
 * 默认的 AnalysisEventListener
 *
 * @author lengleng
 * @author L.cm 2021/4/16
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

	private final List<Object> list = new ArrayList<>();

	private final List<ErrorMessage> errorMessageList = new ArrayList<>();

	@Override
	public void invoke(Object o, AnalysisContext analysisContext) {
		ReadRowHolder readRowHolder = analysisContext.readRowHolder();
		Long lineNum = readRowHolder.getRowIndex().longValue() + 1;

		Set<ConstraintViolation<Object>> violations = this.getValidator().validate(o);
		if (!violations.isEmpty()) {
			Set<String> messageSet = violations.stream()
				.map(ConstraintViolation::getMessage)
				.collect(Collectors.toSet());
			this.errorMessageList.add(new ErrorMessage(lineNum, messageSet));
		}
		else {
			this.list.add(o);
		}
	}

	@Override
	public void doAfterAllAnalysed(AnalysisContext analysisContext) {
		log.debug("Excel read analysed");
	}

	@Override
	public List<Object> getList() {
		return this.list;
	}

	@Override
	public List<ErrorMessage> getErrors() {
		return this.errorMessageList;
	}

}
