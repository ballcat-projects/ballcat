package com.hccake.ballcat.common.xss.core;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.hccake.ballcat.common.xss.cleaner.XssCleaner;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * XSS过滤 jackson 反序列化器
 *
 * @author Hccake 2019/10/17 22:23
 */
@Slf4j
public class XssStringJsonDeserializer extends StringDeserializer {

	private final XssCleaner xssCleaner;

	public XssStringJsonDeserializer(XssCleaner xssCleaner) {
		this.xssCleaner = xssCleaner;
	}

	@Override
	public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		if (p.hasToken(JsonToken.VALUE_STRING)) {
			return getCleanText(p.getText());
		}
		JsonToken t = p.currentToken();
		// [databind#381]
		if (t == JsonToken.START_ARRAY) {
			return _deserializeFromArray(p, ctxt);
		}
		// need to gracefully handle byte[] data, as base64
		if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
			Object ob = p.getEmbeddedObject();
			if (ob == null) {
				return null;
			}
			if (ob instanceof byte[]) {
				return ctxt.getBase64Variant().encode((byte[]) ob, false);
			}
			// otherwise, try conversion using toString()...
			return ob.toString();
		}
		// 29-Jun-2020, tatu: New! "Scalar from Object" (mostly for XML)
		if (t == JsonToken.START_OBJECT) {
			return ctxt.extractScalarFromObject(p, this, _valueClass);
		}
		// allow coercions for other scalar types
		// 17-Jan-2018, tatu: Related to [databind#1853] avoid FIELD_NAME by ensuring it's
		// "real" scalar
		if (t.isScalarValue()) {
			String text = p.getValueAsString();
			return getCleanText(text);
		}
		return (String) ctxt.handleUnexpectedToken(_valueClass, p);
	}

	private String getCleanText(String text) {
		if (text == null) {
			return null;
		}
		return XssStateHolder.enabled() ? xssCleaner.clean(text) : text;
	}

}
