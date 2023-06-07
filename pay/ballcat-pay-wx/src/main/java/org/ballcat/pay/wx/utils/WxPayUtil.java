/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ballcat.pay.wx.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.pay.wx.constants.WxPayConstant;
import org.ballcat.pay.wx.enums.SignType;
import org.bson.types.ObjectId;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lingting 2021/1/26 16:04
 */
@Slf4j
public final class WxPayUtil {

	private WxPayUtil() {
	}

	public static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
		factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
		factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
		factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
		factory.setXIncludeAware(false);
		factory.setExpandEntityReferences(false);
		return factory.newDocumentBuilder();
	}

	public static Document getDocument() throws ParserConfigurationException {
		return getDocumentBuilder().newDocument();
	}

	/**
	 * map 转 xml 字符串
	 * @param data map
	 * @return java.lang.String
	 */
	public static String mapToXml(Map<String, String> data) throws ParserConfigurationException, TransformerException {
		Document document = getDocument();
		Element root = document.createElement("xml");
		document.appendChild(root);
		for (Map.Entry<String, String> entry : data.entrySet()) {
			String value = entry.getValue();
			if (value == null) {
				value = "";
			}
			value = value.trim();
			Element filed = document.createElement(entry.getKey());
			filed.appendChild(document.createTextNode(value));
			root.appendChild(filed);
		}

		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");

		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);

		transformer.transform(new DOMSource(document), result);
		String output = writer.getBuffer().toString();

		try {
			writer.close();
		}
		catch (Exception ex) {
			log.error("string 写入流关闭异常");
		}
		return output;
	}

	/**
	 * xml字符串转map
	 * @param xml xml字符串
	 * @return java.util.Map<java.lang.String,java.lang.String>
	 */
	public static Map<String, String> xmlToMap(String xml)
			throws ParserConfigurationException, IOException, SAXException {
		Map<String, String> data = new HashMap<>(30);

		InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));

		Document doc = getDocumentBuilder().parse(stream);
		doc.getDocumentElement().normalize();
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		for (int idx = 0; idx < nodeList.getLength(); ++idx) {
			Node node = nodeList.item(idx);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element element = (Element) node;
				data.put(element.getNodeName(), element.getTextContent());
			}
		}
		try {
			stream.close();
		}
		catch (Exception ex) {
			// do nothing
		}
		return data;
	}

	/**
	 * 签名
	 * @param params 参数
	 * @param mckKey 密钥
	 * @return java.lang.String 签名结果
	 */
	public static String sign(Map<String, String> params, String mckKey) {
		SignType st = SignType.of(params.get(WxPayConstant.FIELD_SIGN_TYPE));
		if (null == st) {
			throw new IllegalArgumentException("签名类型不能为空!");
		}
		return sign(params, st, mckKey);
	}

	@SneakyThrows({ InvalidKeyException.class, NoSuchAlgorithmException.class })
	public static String sign(Map<String, String> params, SignType type, String mckKey) {
		String[] keyArray = params.keySet().toArray(new String[0]);
		// 参数key排序
		Arrays.sort(keyArray);
		// 构建排序后的用于签名的字符串
		StringBuilder paramsStr = new StringBuilder();
		// 参数值
		String val;
		for (String k : keyArray) {
			if (k.equals(WxPayConstant.FIELD_SIGN)) {
				continue;
			}
			// 参数值为空，则不参与签名
			val = params.get(k);
			if (null != val) {
				paramsStr.append(k).append("=").append(val.trim()).append("&");
			}
		}
		paramsStr.append("key=").append(mckKey);

		// 签名后的字节
		byte[] bytes;
		if (type == SignType.MD5) {
			final MessageDigest md5 = MessageDigest.getInstance("MD5");
			bytes = md5.digest(paramsStr.toString().getBytes(StandardCharsets.UTF_8));
		}
		else {
			final Mac mac = Mac.getInstance("HmacSHA256");
			SecretKeySpec sk = new SecretKeySpec(mckKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
			mac.init(sk);
			bytes = mac.doFinal(paramsStr.toString().getBytes(StandardCharsets.UTF_8));
		}
		// 构建返回值
		StringBuilder builder = new StringBuilder();

		for (byte b : bytes) {
			builder.append(Integer.toHexString((b & 0xFF) | 0x100), 1, 3);
		}
		return builder.toString().toUpperCase();
	}

	/**
	 * 生成随机字符串 32位以内字符串
	 * @return java.lang.String
	 */
	public static String generateNonceStr() {
		return ObjectId.get().toString();
	}

}
