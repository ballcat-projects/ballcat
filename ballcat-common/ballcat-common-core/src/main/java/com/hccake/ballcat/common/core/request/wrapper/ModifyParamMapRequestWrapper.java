package com.hccake.ballcat.common.core.request.wrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;

/**
 * 修改parameterMap
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 21:57
 */
public class ModifyParamMapRequestWrapper extends HttpServletRequestWrapper {

	private final Map<String, String[]> parameterMap;

	public ModifyParamMapRequestWrapper(HttpServletRequest request, Map<String, String[]> parameterMap) {
		super(request);
		this.parameterMap = parameterMap;
	}

	@Override
	public Map<String, String[]> getParameterMap() {
		return this.parameterMap;
	}

}