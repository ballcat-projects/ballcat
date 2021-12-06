package com.hccake.ballcat.common.desensitize.json;

import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Json 脱敏模块
 *
 * @author hccake
 */
public class JsonDesensitizeModule extends SimpleModule {

	public JsonDesensitizeModule(JsonDesensitizeSerializerModifier jsonDesensitizeSerializerModifier) {
		super();
		this.setSerializerModifier(jsonDesensitizeSerializerModifier);
	}

}
