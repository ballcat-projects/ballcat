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

package org.ballcat.web.util;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * 用于修改 Request Parameter 的 HttpServletRequestWrapper
 *
 * @author Hccake 2019/10/17 21:57
 */
public class ModifyParameterRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> parameterMap;

	public ModifyParameterRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap) {
		super(request);
		this.parameterMap = parameterMap;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

	@Override
	public String getParameter(String name) {
		String[] parameters = this.parameterMap.get(name);
		if (parameters != null && parameters.length > 0) {
			return parameters[0];
		}
		return null;
	}

	@Override
	public Enumeration<String> getParameterNames() {
		return Collections.enumeration(this.parameterMap.keySet());
	}

	@Override
	public String[] getParameterValues(String name) {
		return this.parameterMap.get(name);
	}

}
