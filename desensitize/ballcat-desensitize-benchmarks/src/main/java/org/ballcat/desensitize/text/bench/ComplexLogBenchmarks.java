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

package org.ballcat.desensitize.text.bench;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.RegexReplacementParams;
import org.ballcat.desensitize.text.config.RuleSpec;
import org.ballcat.desensitize.text.config.SlideMaskParams;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

/**
 * 复杂日志脱敏基准测试： - 同时包含手机号/身份证号/银行卡号规则 - 多种前缀（中英文）与多种分隔符/包裹符混合 - 命中/仅关键词命中但值未命中/完全未命中混合 -
 * 多线程场景下评估整体吞吐与平均时延
 *
 * @author Hccake
 */
@BenchmarkMode({ Mode.AverageTime })
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@Threads(8)
@State(Scope.Benchmark)
public class ComplexLogBenchmarks {

	@Param({ "1", "10", "128", "1024" })
	public int payloadKB; // 负载大小

	@Param({ "4", "8", "16" })
	public int tokensPerKB; // 每 KB 插入的敏感片段数量（含命中/未命中混合）

	private TextDesensitizer engine;

	private String payload;

	// 样例数据
	private final String samplePhone = "13877891234";

	private final String sampleId18 = "110105199001012345"; // 末尾 '5' 代表最后4位

	private final String sampleBank19 = "6222021234567890123";

	@Setup
	public void setup() {
		// 1) 规则：手机号（正则替换，前三后四）
		RegexReplacementParams phoneRegex = new RegexReplacementParams().setReplacement("$1****$2");
		RuleSpec phoneRule = new RuleSpec().setName("phone")
			.setPrefixes(Arrays.asList("phone", "mobile", "手机号", "手机", "tel", "电话"))
			.setValuePattern("(1[3-9]\\d{3})\\d{4}(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(phoneRegex);

		// 2) 规则：身份证（正则替换，前6后4明文）
		RegexReplacementParams idRegex = new RegexReplacementParams().setReplacement("$1********$2");
		RuleSpec idRule = new RuleSpec().setName("idcard")
			.setPrefixes(Arrays.asList("id", "idNo", "身份证", "身份", "idCard", "证件号"))
			.setValuePattern("([1-9]\\d{5})\\d{8}([0-9Xx]{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(idRegex);

		// 3) 规则：银行卡（滑动脱敏，仅保留后 4 位）
		SlideMaskParams bankSlide = new SlideMaskParams();
		bankSlide.setLeftPlainTextLen(0);
		bankSlide.setRightPlainTextLen(4);
		bankSlide.setMaskString("*");
		RuleSpec bankRule = new RuleSpec().setName("bank")
			.setPrefixes(Arrays.asList("bank", "card", "银行卡", "bankCard", "account", "acct"))
			.setValuePattern("\\d{16,19}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(bankSlide);

		this.engine = new TextDesensitizerBuilder().addRule(phoneRule).addRule(idRule).addRule(bankRule).build();

		buildComplexPayload();
	}

	private void buildComplexPayload() {
		int total = this.payloadKB * 1024;
		int tokens = this.tokensPerKB * this.payloadKB;
		if (tokens <= 0) {
			tokens = 1;
		}

		// 命中 tokens（前缀+有效值，分隔符/包裹符多样）
		List<String> valid = Arrays.asList("手机号：" + this.samplePhone, "phone: " + this.samplePhone,
				"mobile=\"" + this.samplePhone + "\"", "tel='" + this.samplePhone + "'",

				"身份证：" + this.sampleId18, "idNo: " + this.sampleId18, "idCard=\"" + this.sampleId18 + "\"",

				"银行卡=【" + this.sampleBank19 + "】", "bank:[" + this.sampleBank19 + "]",
				"account=\"" + this.sampleBank19 + "\"");

		// 仅关键词命中但值未命中（长度/字符不符）
		List<String> acOnly = Arrays.asList("手机号：12345", "mobile=\"abc\"", "idNo: 11010519900101", "证件号='XYZ'",
				"bank=\"1234\"", "acct:[0000]");

		// 无前缀，仅值或随机片段
		List<String> noise = Arrays.asList(this.sampleBank19, "lorem ipsum dolor sit amet", "{\"k\":\"v\"}",
				"路径=/api/order?id=123", "注意：测试文本");

		int avgGap = Math.max(1, (total - tokens * 32) / tokens);
		StringBuilder sb = new StringBuilder(total + 256);
		int vi = 0;
		int ai = 0;
		int ni = 0;
		for (int i = 0; i < tokens; i++) {
			for (int j = 0; j < avgGap; j++) {
				sb.append('a');
			}
			// 交错插入：2 个命中 + 1 个 ACOnly + 1 个噪声
			sb.append(' ').append(valid.get(vi++ % valid.size())).append(' ');
			sb.append(' ').append(valid.get(vi++ % valid.size())).append(' ');
			sb.append(' ').append(acOnly.get(ai++ % acOnly.size())).append(' ');
			sb.append(' ').append(noise.get(ni++ % noise.size())).append(' ');
		}
		while (sb.length() < total) {
			sb.append('x');
		}
		this.payload = sb.toString();
	}

	@Benchmark
	public String sanitize_complex_payload() {
		return this.engine.desensitize(this.payload);
	}

}
