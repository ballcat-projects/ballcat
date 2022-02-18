package com.hccake.ballcat.common.xss.core;

import com.hccake.ballcat.common.xss.cleaner.XssCleaner;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Request包装类: 用于 XSS 过滤
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 10:29
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
				values[i] = xssCleaner.clean(values[i]);
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
			encodedValues[i] = xssCleaner.clean(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return xssCleaner.clean(value);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value instanceof String) {
			xssCleaner.clean((String) value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return xssCleaner.clean(value);
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null) {
			return null;
		}
		return xssCleaner.clean(value);
	}

}
