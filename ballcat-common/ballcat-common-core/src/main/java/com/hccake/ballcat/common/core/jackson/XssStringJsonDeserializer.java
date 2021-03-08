package com.hccake.ballcat.common.core.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hccake.ballcat.common.util.HtmlUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:23 XSS过滤
 */
@Slf4j
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getValueAsString();
		if (value != null) {
			return HtmlUtils.cleanUnSafe(value);
		}
		return null;
	}

	@Override
	public Class<String> handledType() {
		return String.class;
	}

}
