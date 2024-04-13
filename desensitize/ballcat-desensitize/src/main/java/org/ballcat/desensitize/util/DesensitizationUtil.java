/*
 * Copyright 2023-2024 the original author or authors.
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

package org.ballcat.desensitize.util;

import org.ballcat.desensitize.DesensitizationHandlerHolder;
import org.ballcat.desensitize.enums.RegexDesensitizationTypeEnum;
import org.ballcat.desensitize.enums.SlideDesensitizationTypeEnum;
import org.ballcat.desensitize.handler.IPDesensitizationHandler;
import org.ballcat.desensitize.handler.RegexDesensitizationHandler;
import org.ballcat.desensitize.handler.RuleDesensitizationHandler;
import org.ballcat.desensitize.handler.SimpleDesensitizationHandler;
import org.ballcat.desensitize.handler.SixAsteriskDesensitizationHandler;
import org.ballcat.desensitize.handler.SlideDesensitizationHandler;

/**
 * 脱敏工具类
 * <ul>
 * <li>支持常用类型的脱敏：如姓名、身份证、银行卡号、手机号、密码、加密密文、座机、邮箱、地址、IP等</li>
 * <li>支持自定义前后保留多少位脱敏，可自定义脱敏占位符</li>
 * <li>支持基于自定义规则的脱敏：如指定"3-6，8，10-"表示第4，5，6，7，9，11以及11之后的位使用加密字符替换</li>
 * </ul>
 *
 * @author evil0th Create on 2024/4/12
 */
public final class DesensitizationUtil {

	private DesensitizationUtil() {
	}

	/**
	 * 中文姓名只显示第一个姓和最后一个汉字（单名则只显示最后一个汉字），其他隐藏为星号 <pre>
	 *     DesensitizationUtil.desensitizeChineseName("张梦") = "*梦"
	 *     DesensitizationUtil.desensitizeChineseName("张小梦") = "张*梦"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeChineseName(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeBySlide(input, input.length() > 2 ? 1 : 0, 1);
	}

	/**
	 * 身份证(18位或者15位)显示前六位, 四位，其他隐藏。 <pre>
	 *     DesensitizationUtil.desensitizeIdCardNo("43012319990101432X") = "430123********432X"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeIdCardNo(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeBySlide(input, SlideDesensitizationTypeEnum.ID_CARD_NO);
	}

	/**
	 * 移动电话前三位，后四位，其他隐藏，比如 <pre>
	 * DesensitizationUtil.desensitizePhoneNumber("13812345678") = "138******10"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizePhoneNumber(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeBySlide(input, SlideDesensitizationTypeEnum.PHONE_NUMBER);
	}

	/**
	 * 地址脱敏，只显示到地区，不显示详细地址 <pre>
	 * DesensitizationUtil.desensitizeAddress("北京市西城区金城坊街2号") = "北京市西城区******"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeAddress(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeBySlide(input, 6, 0);
	}

	/**
	 * 电子邮箱脱敏，邮箱前缀最多显示前1字母，前缀其他隐藏，用星号代替，@及后面的地址显示 <pre>
	 * DesensitizationUtil.desensitizeEmail("test.demo@qq.com") = "t****@qq.com"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeEmail(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeByRegex(input, RegexDesensitizationTypeEnum.EMAIL);
	}

	/**
	 * 银行卡号脱敏，显示前六位后四位 <pre>
	 * DesensitizationUtil.desensitizeBankCardNo("62226000000043211234") = "622260**********1234"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBankCardNo(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeBySlide(input, SlideDesensitizationTypeEnum.BANK_CARD_NO);
	}

	/**
	 * 密码脱敏，用******代替 <pre>
	 * DesensitizationUtil.desensitizePassword(password) = "******"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizePassword(String input) {
		return desensitizeBySimpleHandler(input, SixAsteriskDesensitizationHandler.class);
	}

	/**
	 * IPv脱敏，支持IPv4和IPv6 <pre>
	 * DesensitizationUtil.desensitizeIP("192.168.2.1") = "192.*.*.*"
	 * DesensitizationUtil.desensitizeIP("2001:0db8:02de:0000:0000:0000:0000:0e13") = "2001:*:*:*:*:*:*:*"
	 * DesensitizationUtil.desensitizeIP("2001:db8:2de:0000:0000:0000:0000:e13") = "2001:*:*:*:*:*:*:*"
	 * DesensitizationUtil.desensitizeIP("2001:db8:2de:0:0:0:0:e13") = "2001:*:*:*:*:*:*:*"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeIP(String input) {
		return desensitizeBySimpleHandler(input, IPDesensitizationHandler.class);
	}

	/**
	 * 密文脱敏，前3后2，中间替换为 4个 *
	 *
	 * <pre>
	 * DesensitizationUtil.desensitizeKey("0000000123456q34") = "000****34"
	 * </pre>
	 * @param input 待处理的文本
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeKey(String input) {
		if (isEmptyText(input)) {
			return input;
		}
		return desensitizeByRegex(input, RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD);
	}

	/**
	 * 使用简单脱敏处理器进行脱敏。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeBySimpleHandler(password, SixAsteriskDesensitizationHandler.class) = "******"
	 * </pre>
	 * @param input 输入字符串
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySimpleHandler(String input,
			Class<? extends SimpleDesensitizationHandler> handlerClass) {
		if (isEmptyText(input)) {
			return input;
		}
		SimpleDesensitizationHandler simpleHandler = DesensitizationHandlerHolder.getSimpleHandler(handlerClass);
		return simpleHandler.handle(input);
	}

	/**
	 * 根据正则进行打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeByRegex("test.demo@qq.com", RegexDesensitizationTypeEnum.EMAIL) = "t****@qq.com"
	 * </pre>
	 * @param input 输入字符串
	 * @param type {@link RegexDesensitizationTypeEnum}
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeByRegex(String input, RegexDesensitizationTypeEnum type) {
		RegexDesensitizationHandler regexHandler = DesensitizationHandlerHolder.getRegexDesensitizationHandler();
		return regexHandler.handle(input, type);
	}

	/**
	 * 根据正则进行打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeByRegex("test.demo@qq.com", RegexDesensitizationTypeEnum.EMAIL) = "t****@qq.com"
	 * </pre>
	 * @param input 输入字符串
	 * @param regex 正则
	 * @param replacement 替换模板
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeByRegex(String input, String regex, String replacement) {
		RegexDesensitizationHandler regexHandler = DesensitizationHandlerHolder.getRegexDesensitizationHandler();
		return regexHandler.handle(input, regex, replacement);
	}

	/**
	 * 滑动打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeBySlide("01089898976", SlideDesensitizationTypeEnum.PHONE_NUMBER) = "010******76"
	 * </pre>
	 * @param input 输入字符串
	 * @param type {@link SlideDesensitizationTypeEnum}
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, SlideDesensitizationTypeEnum type) {
		return desensitizeBySlide(input, type, false);
	}

	/**
	 * 滑动打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeBySlide("01089898976", SlideDesensitizationTypeEnum.PHONE_NUMBER, true) = "***898989**"
	 * </pre>
	 * @param input 输入字符串
	 * @param type {@link SlideDesensitizationTypeEnum}
	 * @param reverse 是否反转
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, SlideDesensitizationTypeEnum type, boolean reverse) {
		SlideDesensitizationHandler slideHandler = DesensitizationHandlerHolder.getSlideDesensitizationHandler();
		return slideHandler.handle(input, type, reverse);
	}

	/**
	 * 滑动打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeBySlide("Hello World", 2, 3) = "He******rld"
	 * </pre>
	 * @param input 输入字符串
	 * @param head 头部保留长度
	 * @param tail 尾部保留长度
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, int head, int tail) {
		return desensitizeBySlide(input, head, tail, false);
	}

	/**
	 * 滑动打码。
	 *
	 * <pre>
	 *     DesensitizationUtil.desensitizeBySlide("Hello World", 2, 3) = "He******rld"
	 * </pre>
	 * @param input 输入字符串
	 * @param head 头部保留长度
	 * @param tail 尾部保留长度
	 * @param reverse 是否反转
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, int head, int tail, boolean reverse) {
		return desensitizeBySlide(input, head, tail, "*", reverse);
	}

	/**
	 * 滑动打码。 <pre>
	 * DesensitizationUtil.desensitizeBySlide("Hello World", 2, 3, "#") = "He######rld"
	 * </pre>
	 * @param input 输入字符串
	 * @param head 头部保留长度
	 * @param tail 尾部保留长度
	 * @param maskString 替换结果字符
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, int head, int tail, String maskString) {
		return desensitizeBySlide(input, head, tail, maskString, false);
	}

	/**
	 * 滑动打码。 <pre>
	 * DesensitizationUtil.desensitizeBySlide("Hello World", 2, 3, "#") = "He######rld"
	 * </pre>
	 * @param input 输入字符串
	 * @param head 头部保留长度
	 * @param tail 尾部保留长度
	 * @param maskString 替换结果字符
	 * @return 屏蔽后的文本
	 */
	public static String desensitizeBySlide(String input, int head, int tail, String maskString, boolean reverse) {
		if (isEmptyText(input)) {
			return input;
		}
		if (head + tail >= input.length()) {
			return input;
		}
		SlideDesensitizationHandler slideHandler = DesensitizationHandlerHolder.getSlideDesensitizationHandler();
		return slideHandler.handle(input, head, tail, maskString, reverse);
	}

	/**
	 * 基于规则的替换字符串 <pre>
	 *     DesensitizationUtil.desensitizeByRule("43012319990101432X", "1", "4-6", "9-")) = "4*01***99*********"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则。<br>
	 * @return 脱敏字符串
	 */
	public static String desensitizeByRule(String input, String... rule) {
		final RuleDesensitizationHandler ruleHandler = DesensitizationHandlerHolder.getRuleDesensitizationHandler();
		return ruleHandler.handle(input, rule);
	}

	/**
	 * 基于规则的替换字符串 <pre>
	 *     DesensitizationUtil.desensitizeByRule("43012319990101432X", true, "1", "4-6", "9-")) = "4*01***99*********"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则
	 * @param reverse 是否反转规则
	 * @return 脱敏字符串
	 */
	public static String desensitizeByRule(String input, boolean reverse, String... rule) {
		final RuleDesensitizationHandler ruleHandler = DesensitizationHandlerHolder.getRuleDesensitizationHandler();
		return ruleHandler.handle(input, reverse, rule);
	}

	/**
	 * 基于规则的替换字符串 <pre>
	 *     DesensitizationUtil.desensitizeByRule("43012319990101432X", '-', false, "1", "4-6", "9-")) = "4-01---99---------"
	 *     DesensitizationUtil.desensitizeByRule("43012319990101432X", '-', true, "1", "4-6", "9-")) = "-3--231--90101432X"
	 * </pre>
	 * @param input 输入字符串
	 * @param rule 规则
	 * @param symbol 符号，默认*
	 * @param reverse 是否反转规则
	 * @return 脱敏字符串
	 */
	public static String desensitizeByRule(String input, char symbol, boolean reverse, String... rule) {
		final RuleDesensitizationHandler ruleHandler = DesensitizationHandlerHolder.getRuleDesensitizationHandler();
		return ruleHandler.handle(input, symbol, reverse, rule);
	}

	/**
	 * 判断是否无效字符串
	 * @param text 字符串
	 * @return true-无效字符串
	 */
	private static boolean isEmptyText(String text) {
		return null == text || text.isEmpty();
	}

}
