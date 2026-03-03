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

import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.compiler.CompiledRule;
import org.ballcat.desensitize.text.config.SlideMaskParams;
import org.ballcat.desensitize.util.DesensitizationUtil;

/**
 * 脱敏引擎对外 API：对文本消息进行按规则的脱敏处理。 线程安全，构建后不可变。
 *
 * @author Hccake
 * @since 2.0.0
 */
public class TextDesensitizer {

	private final List<CompiledRule> rules; // in configured order

	private final BoundaryLocator boundaryLocator;

	private final PrefixScanner acScanner; // AC 前缀扫描

	private final PrefixRuleIndex prefixRuleIndex; // 前缀 -> 规则下标候选

	public TextDesensitizer(List<CompiledRule> rules, PrefixScanner acScanner, BoundaryLocator boundaryLocator,
			PrefixRuleIndex prefixRuleIndex) {
		this.rules = rules;
		this.acScanner = acScanner;
		this.boundaryLocator = boundaryLocator;
		this.prefixRuleIndex = prefixRuleIndex;
	}

	public String desensitize(CharSequence raw) {
		if (raw == null) {
			return null;
		}

		int len = raw.length();
		if (len == 0 || this.rules.isEmpty()) {
			return raw.toString();
		}

		List<PrefixScanner.Hit> hits = this.acScanner.findAll(raw);
		if (hits.isEmpty()) {
			return raw.toString();
		}

		// 根据起始位置排序，保证从左到右处理
		hits.sort(Comparator.comparingInt(h -> h.start));

		StringBuilder out = new StringBuilder(len + 16);
		int cursor = 0; // 原文游标

		for (PrefixScanner.Hit h : hits) {
			int prefixStart = h.start;
			int prefixEnd = h.endExclusive; // endExclusive in original
			if (prefixStart < cursor) {
				continue; // 已被之前的替换覆盖
			}

			// 获取 value 区间
			BoundaryLocator.Boundary boundary = this.boundaryLocator.locate(raw, prefixEnd);
			int valueStart = boundary.getStart();
			if (valueStart < 0) {
				continue;
			}
			int endExclusive = boundary.getEndExclusive();

			// 按配置顺序尝试与当前命中前缀关联的规则，取首个在窗口内匹配的规则
			int appliedStart = -1;
			int appliedEnd = -1;
			CompiledRule appliedRule = null;

			// 仅尝试与本次命中的前缀关键字对应的候选规则（按配置顺序）
			int[] candidates = this.prefixRuleIndex.candidatesFor(h.keyword.toLowerCase(java.util.Locale.ROOT));
			for (int idxRule : candidates) {
				CompiledRule cr = this.rules.get(idxRule);
				Matcher m = cr.getValuePatternCompiled().matcher(raw).region(valueStart, endExclusive);
				boolean matchFromStart = cr.isMatchFromStart();
				// 如果强制匹配从开头开始，则使用 lookingAt，否则使用 find
				if (matchFromStart ? m.lookingAt() : m.find()) {
					appliedStart = m.start();
					appliedEnd = m.end();
					appliedRule = cr;
					break;
				}
			}

			if (appliedStart >= 0) {
				// 写入匹配前的原文片段
				out.append(raw, cursor, appliedStart);
				// 直接使用第一次命中的区间 [appliedStart, appliedEnd) 作为替换范围
				CharSequence matched = raw.subSequence(appliedStart, appliedEnd);
				String replaced;
				DesensitizeType ruleDesensitizeType = appliedRule.getDesensitizeType();
				if (DesensitizeType.REGEX_REPLACEMENT.equals(ruleDesensitizeType)) {
					Matcher mm2 = appliedRule.getValuePatternCompiled().matcher(matched);
					replaced = mm2.replaceFirst(appliedRule.getRegex().getReplacement());
				}
				else if (DesensitizeType.SLIDE_MASK.equals(ruleDesensitizeType)) {
					SlideMaskParams pm = appliedRule.getSlide();
					replaced = DesensitizationUtil.maskBySlide(matched.toString(), pm.getLeftPlainTextLen(),
							pm.getRightPlainTextLen(), pm.getMaskString());
				}
				else if (DesensitizeType.SIMPLE_HANDLE.equals(ruleDesensitizeType)) {
					replaced = DesensitizationUtil.maskBySimpleHandler(matched.toString(),
							appliedRule.getSimple().getHandler());
				}
				else {
					// fallback
					replaced = matched.toString();
				}
				out.append(replaced);
				cursor = appliedEnd;
			}
		}

		if (cursor < len) {
			out.append(raw, cursor, len);
		}
		return out.toString();
	}

}
