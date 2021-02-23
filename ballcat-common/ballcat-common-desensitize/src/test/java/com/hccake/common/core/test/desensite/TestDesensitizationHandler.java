package com.hccake.common.core.test.desensite;

import com.hccake.ballcat.common.desensitize.handler.SimpleDesensitizationHandler;

/**
 * @author Hccake 2021/1/23
 * @version 1.0
 */
public class TestDesensitizationHandler implements SimpleDesensitizationHandler {

	@Override
	public String handle(String s) {
		return "TEST-" + s;
	}

}
