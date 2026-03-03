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

package org.ballcat.desensitize.text;

import java.util.Arrays;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.config.BoundaryOptions;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.ballcat.desensitize.text.config.SlideMaskParams;

/**
 * 测试构造工具：提供常用规则与长文本构造。
 *
 * @author Hccake
 */
public final class TestFixtures {

	private TestFixtures() {
	}

	public static RuleSpec phoneRegexRule() {
		RegexReplacementParams rp = new RegexReplacementParams().setReplacement("$1****$3");
		return new RuleSpec().setName("phone")
			.setPrefixes(Arrays.asList("phone", "mobile", "手机号", "tel"))
			.setValuePattern("(1[3-9]\\d)(\\d{4})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(rp);
	}

	public static RuleSpec idRegexRule() {
		RegexReplacementParams rp = new RegexReplacementParams().setReplacement("$1***********$2");
		return new RuleSpec().setName("id")
			.setPrefixes(Arrays.asList("id", "身份证", "idNo", "idCard", "证件号"))
			.setValuePattern("([1-9]\\d{3})\\d{11}([0-9Xx]{3})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(rp);
	}

	public static RuleSpec bankSlideRule() {
		SlideMaskParams sp = new SlideMaskParams();
		sp.setLeftPlainTextLen(0);
		sp.setRightPlainTextLen(4);
		sp.setMaskString("*");
		return new RuleSpec().setName("bank")
			.setPrefixes(Arrays.asList("bank", "bankCard", "银行卡", "account", "acct"))
			.setValuePattern("\\d{16,19}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(sp);
	}

	public static BoundaryOptions defaultMatcherOptions() {
		return new BoundaryOptions();
	}

	public static String buildLongPayload(int totalChars, String token, int tokenRepeat) {
		StringBuilder sb = new StringBuilder(totalChars + 128);
		int avgGap = tokenRepeat > 0 ? Math.max(1, (totalChars - tokenRepeat * token.length()) / tokenRepeat)
				: totalChars;
		for (int i = 0; i < tokenRepeat; i++) {
			for (int j = 0; j < avgGap; j++) {
				sb.append('a');
			}
			sb.append(token);
		}
		while (sb.length() < totalChars) {
			sb.append('x');
		}
		return sb.toString();
	}

}
