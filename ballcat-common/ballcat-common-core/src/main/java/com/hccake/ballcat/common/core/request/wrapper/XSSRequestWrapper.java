package com.hccake.ballcat.common.core.request.wrapper;

import com.hccake.ballcat.common.util.HtmlUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 10:29 Request包装类 1. XSS过滤
 */
@Slf4j
public class XssRequestWrapper extends HttpServletRequestWrapper {

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> map = new LinkedHashMap<>();
		Map<String, String[]> parameters = super.getParameterMap();
		for (Map.Entry<String, String[]> entry : parameters.entrySet()) {
			String[] values = entry.getValue();
			for (int i = 0; i < values.length; i++) {
				values[i] = HtmlUtils.cleanUnSafe(values[i]);
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
			encodedValues[i] = HtmlUtils.cleanUnSafe(values[i]);
		}
		return encodedValues;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(name);
		if (value == null) {
			return null;
		}
		return HtmlUtils.cleanUnSafe(value);
	}

	@Override
	public Object getAttribute(String name) {
		Object value = super.getAttribute(name);
		if (value instanceof String) {
			HtmlUtils.cleanUnSafe((String) value);
		}
		return value;
	}

	@Override
	public String getHeader(String name) {
		String value = super.getHeader(name);
		if (value == null) {
			return null;
		}
		return HtmlUtils.cleanUnSafe(value);
	}

	@Override
	public String getQueryString() {
		String value = super.getQueryString();
		if (value == null) {
			return null;
		}
		return HtmlUtils.cleanUnSafe(value);
	}

}
