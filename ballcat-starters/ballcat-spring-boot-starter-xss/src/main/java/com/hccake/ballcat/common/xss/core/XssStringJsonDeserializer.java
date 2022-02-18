package com.hccake.ballcat.common.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.hccake.ballcat.common.xss.cleaner.XssCleaner;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * XSS过滤 jackson 反序列化器
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 22:23
 */
@Slf4j
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

	private final XssCleaner xssCleaner;

	public XssStringJsonDeserializer(XssCleaner xssCleaner) {
		this.xssCleaner = xssCleaner;
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String value = p.getValueAsString();
		// 没开启 Xss 则直接返回
		if (!XssStateHolder.enabled()) {
			return value;
		}
		return value != null ? xssCleaner.clean(value) : null;
	}

	@Override
	public Class<String> handledType() {
		return String.class;
	}

}
