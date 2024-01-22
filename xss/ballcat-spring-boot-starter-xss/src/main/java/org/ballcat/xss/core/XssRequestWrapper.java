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

package org.ballcat.xss.core;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.xss.cleaner.XssCleaner;

/**
 * Request包装类: 用于 XSS 过滤
 *
 * @author Hccake 2019/10/16 10:29
 */
@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {

	private final XssCleaner xssCleaner;

	public XssRequestWrapper(HttpServletRequest request, XssCleaner xssCleaner) {
		super(request);
		this.xssCleaner = xssCleaner;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = this.xssCleaner.clean(values[i]);
			}
			map.put(entry.getKey(), values);
		}
		return map;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) {
			return null;
		}
		int count = values.length;
		String[] encodedValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodedValues[i] = this.xssCleaner.clean(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return this.xssCleaner.clean(value);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value instanceof String) {
			this.xssCleaner.clean((String) value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return this.xssCleaner.clean(value);
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null) {
			return null;
		}
		return this.xssCleaner.clean(value);
	}

}
