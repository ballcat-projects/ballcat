package com.hccake.common.core.test.desensite.custom;

import com.hccake.ballcat.common.desensitize.handler.DesensitizationHandler;

public class CustomDesensitisedHandler implements DesensitizationHandler {

	public String handle(String text) {
		return "customer rule" + text;
	}

}
