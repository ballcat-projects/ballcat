package com.hccake.ballcat.common.xss.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.hccake.ballcat.common.util.HtmlUtils;

import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:23 XSS过滤
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
			throws IOException {
		if (value != null) {
			// 开启 Xss 才进行处理
			if (XssStateHolder.enabled()) {
				value = HtmlUtils.cleanUnSafe(value);
			}
			jsonGenerator.writeString(value);
		}
	}

}
