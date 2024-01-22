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

package org.ballcat.common.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.NonNull;
import org.ballcat.common.constant.Symbol;

/**
 * 正则表达式工具集
 *
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/9
 */
public final class RegexUtils {

	private RegexUtils() {
	}

	/**
	 * 日期
	 */
	public static final String RE_DATE = "^((((1[6-9]|[2-9]\\d)\\d{2})-(0?[13578]|1[02])-(0?[1-9]|[12]\\d|3[01]))|(((1[6-9]|[2-9]\\d)\\d{2})-(0?[13456789]|1[012])-(0?[1-9]|[12]\\d|30))|(((1[6-9]|[2-9]\\d)\\d{2})-0?2-(0?[1-9]|1\\d|2[0-8]))|(((1[6-9]|[2-9]\\d)(0[48]|[2468][048]|[13579][26])|((16|[2468][048]|[3579][26])00))-0?2-29-)) (20|21|22|23|[0-1]?\\d):[0-5]?\\d:[0-5]?\\d$";

	/**
	 * 身份证号码
	 */
	public static final String RE_CITIZEN_ID = "^[1-9][0-9]{5}([1][9][0-9]{2}|[2][0][0|1][0-9])([0][1-9]|[1][0|12])([0][1-9]|[1|2][0-9]|[3][0|1])[0-9]{3}([0-9]|[X])$";

	/**
	 * 邮件，符合<a href="http://emailregex.com/">RFC 5322</a>规范
	 */
	public static final String RE_EMAIL = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)])";

	/**
	 * 正则中需要被转义的关键字
	 */
	public static final Set<Character> RE_KEYS = Arrays
		.stream(new Character[] { '$', '(', ')', '*', '+', '.', '[', ']', '?', '\\', '^', '{', '}', '|' })
		.collect(Collectors.toSet());

	/**
	 * 中国车牌号码
	 */
	public static final String RE_PLATE_NUMBER = "^(([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]"
			+ "[A-Z](([0-9]{5}[ABCDEFGHJK])|([ABCDEFGHJK]([A-HJ-NP-Z0-9])[0-9]{4})))|"
			+ "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领]\\d{3}\\d{1,3}[领]?)|"
			+ "([京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-HJ-NP-Z0-9]{4}[A-HJ-NP-Z0-9挂学警港澳使领]))$";

	/**
	 * 十六进制字符串
	 */
	public static final String RE_HEX = "^[a-fA-F0-9]+$";

	/**
	 * 整数
	 */
	public static final String RE_INTEGER = "^-?[1-9]\\d*$";

	/**
	 * 正整数
	 */
	public static final String RE_INTEGER_POSITIVE = "^[1-9]\\d*$";

	/**
	 * 非正整数
	 */
	public static final String RE_INTEGER_POSITIVE_REVERSE = "^-[1-9]\\d*|0$";

	/**
	 * 负整数
	 */
	public static final String RE_INTEGER_NEGATIVE = "^-[1-9]\\d*$";

	/**
	 * 非负整数
	 */
	public static final String RE_INTEGER_NEGATIVE_REVERSE = "^[1-9]\\d*|0$";

	/**
	 * 浮点数
	 */
	public static final String RE_FLOAT = "^-?[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * 正浮点数
	 */
	public static final String RE_FLOAT_POSITIVE = "^[1-9]\\d*\\.\\d*|0\\.\\d*[1-9]\\d*$";

	/**
	 * 负浮点数
	 */
	public static final String RE_FLOAT_NEGATIVE = "^-[1-9]\\d*\\.\\d*|-0\\.\\d*[1-9]\\d*$";

	/**
	 * 给定内容是否匹配正则(matches)
	 * @param regex 正则
	 * @param input 内容
	 * @return 是否匹配
	 */
	public static boolean match(@NonNull String regex, @NonNull CharSequence input) {
		Pattern pattern = getCache(regex, 0);
		return pattern.matcher(input).matches();
	}

	/**
	 * 给定内容是否匹配正则(find)
	 * <p>
	 * <b>字符串某个部分匹配上模式就会返回true</b>
	 * @param regex 正则
	 * @param input 内容
	 * @return 是否匹配
	 */
	public static boolean find(@NonNull String regex, @NonNull CharSequence input) {
		Pattern pattern = getCache(regex, Pattern.CASE_INSENSITIVE);
		return pattern.matcher(input).find();
	}

	/**
	 * 取得内容中匹配的所有结果。
	 * @param regex 正则
	 * @param input 被查找的内容
	 * @return 结果集
	 */
	public static List<String> group(@NonNull String regex, @NonNull CharSequence input) {
		List<String> matches = new ArrayList<>();
		Pattern pattern = getCache(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		int i = 1;
		while (matcher.find()) {
			matches.add(matcher.group(i));
			i++;
		}
		return matches;
	}

	/**
	 * 取得内容中匹配的第一个结果。
	 * @param regex 正则
	 * @param input 被查找的内容
	 * @return 结果集
	 */
	public static String groupFirst(@NonNull String regex, @NonNull CharSequence input) {
		return group(regex, input, 1);
	}

	/**
	 * 取得内容中匹配的第N个结果。
	 * @param regex 正则
	 * @param input 被查找的内容
	 * @param groupIndex 匹配正则的分组序号
	 * @return 匹配后得到的字符串
	 */
	public static String group(@NonNull String regex, @NonNull CharSequence input, int groupIndex) {
		Pattern pattern = getCache(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(input);
		if (matcher.find()) {
			return matcher.group(groupIndex);
		}
		return null;
	}

	/**
	 * 正则替换指定值。
	 * @param input 被替换文本
	 * @param regex 正则
	 * @param replacement 替换的文本
	 * @return 处理后的文本
	 */
	public static String replaceAll(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
		Pattern pattern = getCache(regex, 0);
		return pattern.matcher(input).replaceAll(replacement);
	}

	/**
	 * 正则替换第一个匹配指定值
	 * @param input 被替换文本
	 * @param regex 正则
	 * @param replacement 替换的文本
	 * @return 处理后的文本
	 */
	public static String replaceFirst(@NonNull String regex, @NonNull CharSequence input, @NonNull String replacement) {
		Pattern pattern = getCache(regex, Pattern.DOTALL);
		return pattern.matcher(input).replaceFirst(replacement);
	}

	/**
	 * 是否身份证 1-6位：表示行政区划的代码。 1、2位，所在省（直辖市，自治区）代码； 3、4位，所在地级市（自治州）代码；
	 * 5、6位，所在区（县，自治县，县级市）的代码； 7-14位：表示出生年、月、日 15-16位：所在地派出所代码
	 * 17位：性别。奇数（1、3、5、7、9）男性，偶数（2、4、6、8、0）女性
	 * 18位：校验位，存在十一个值：0,1,2,3,4,5,6,7,8,9,X，其值是用固定公式根据前面十七位计算出来的。
	 * <p>
	 * 验证规则： 第一位不可能是0 第二位到第六位可以是0-9 第七位到第十位是年份，所以七八位为19或者20 十一位和十二位是月份，这两位是01-12之间的数值
	 * 十三位和十四位是日期，是从01-31之间的数值 十五，十六，十七都是数字0-9 十八位可能是数字0-9，也可能是X
	 * @param content 内容
	 * @return 正则为null或者""则不检查，返回true，内容为null返回false
	 */
	public static boolean isCitizenId(String content) {
		if (content == null) {
			// 提供null的字符串为不匹配
			return false;
		}
		// 判断格式是否正确
		boolean match = match(RE_CITIZEN_ID, content);
		if (!match) {
			return false;
		}
		// 加权因子
		final int[] weight = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		// 校验码
		final char[] check = { '1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2' };
		char lastChar = content.charAt(17);
		String pre = content.substring(0, 17);
		// ISO 7064:1983.MOD 11-2
		// 判断最后一位校验码是否正确
		char[] array = pre.toCharArray();
		int len = array.length;
		int sum = 0;
		for (int i = 0; i < len; i++) {
			sum += (array[i] - 48) * weight[i];
		}
		// 校验码[余数]
		char checkChar = check[sum % 11];
		// 返回验证结果，校验码和格式同时正确才算是合法的身份证号码
		return lastChar == checkChar;
	}

	/**
	 * 是否是有效的统一社会信用代码。
	 * @param socialCreditCode 统一社会信用代码
	 * @return 符合返回true，否则返回false
	 */
	public static boolean isSocialCreditCode(String socialCreditCode) {
		if (null == socialCreditCode) {
			return false;
		}
		if (!match("[0123456789ABCDEFGHJKLMNPQRTUWXY]{18}", socialCreditCode)) {
			return false;
		}
		final int[] weight = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };
		final int lastCharIndex = 17;
		final char[] businessCodeArray = socialCreditCode.toCharArray();
		final char check = businessCodeArray[lastCharIndex];
		int sum = 0;
		for (int i = 0; i < lastCharIndex; i++) {
			char key = businessCodeArray[i];
			sum += (Symbol.NUMBER_CHARACTER.indexOf(key) * weight[i]);
		}
		int value = 31 - sum % 31;
		return check == Symbol.NUMBER_CHARACTER.charAt(value % 31);
	}

	/**
	 * 正则编译缓存
	 */
	private static final ConcurrentHashMap<String, Pattern> CACHE = new ConcurrentHashMap<>();

	/**
	 * 获取缓存，如果获取不到，先编译后存入缓存
	 * @param regex 正则字符串
	 * @param flags 匹配规则
	 * @return 正则表达式
	 */
	public static Pattern getCache(String regex, int flags) {
		Pattern pattern = CACHE.get(regex);
		if (pattern == null) {
			pattern = Pattern.compile(regex, flags);
			CACHE.put(regex, pattern);
		}
		return pattern;
	}

	/**
	 * 移除缓存
	 * @param regex 正则字符串
	 * @return 正则表达式
	 */
	public static Pattern removeCache(String regex) {
		return CACHE.remove(regex);
	}

	/**
	 * 清空缓存
	 */
	public static void clearCache() {
		CACHE.clear();
	}

}
