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
import java.util.concurrent.TimeUnit;

import org.ballcat.desensitize.DesensitizeType;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.ballcat.desensitize.text.TextDesensitizerBuilder;
import org.ballcat.desensitize.text.config.BoundaryOptions;
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
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode({ Mode.AverageTime })
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class TextDesensitizerBenchmarks {

	@Param({ "10240", "51200", "204800" })
	public int logSize;

	@Param({ "0", "1", "10", "50" })
	public int numSensitive;

	private TextDesensitizer engine;

	private String input; // 正常：含前缀且值命中

	private String inputACOnly; // 仅前缀命中，窗口内无有效值

	private String inputNoPrefix; // 无前缀命中

	@Setup
	public void setup() {
		RuleSpec phone = new RuleSpec().setName("phone")
			.setPrefixes(Arrays.asList("phone", "mobile", "手机号"))
			.setValuePattern("(?<!\\d)(1[3-9]\\d)(\\d{4})(\\d{4})(?!\\d)")
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(new RegexReplacementParams().setReplacement("$1****$3"));
		RuleSpec bank = new RuleSpec().setName("bank-card")
			.setPrefixes(Arrays.asList("银行卡", "bankCard", "bank"))
			.setValuePattern("\\d{16,19}")
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(new SlideMaskParams(0, 4, "*", false))
			.setMatchFromStart(true);
		this.engine = new TextDesensitizerBuilder().addRule(phone)
			.addRule(bank)
			.config(BoundaryOptions.defaults())
			.build();

		StringBuilder sb = new StringBuilder(this.logSize + 512);
		int interval = this.numSensitive == 0 ? this.logSize : Math.max(1, this.logSize / this.numSensitive);
		for (int i = 0; i < this.logSize; i++) {
			if (this.numSensitive > 0 && i % interval == 0) {
				switch ((i / interval) % 3) {
					case 0:
						sb.append(" 手机号：13877891234 ");
						break;
					case 1:
						sb.append(" bank=[6222021234567890123] ");
						break;
					default:
						sb.append(" {\"phone\":\"13877891234\"} ");
						break;
				}
			}
			else {
				sb.append('a');
			}
		}
		this.input = sb.toString();

		// 构造仅 AC 命中的输入：有前缀与分隔符，但值不满足正则
		StringBuilder acOnly = new StringBuilder(this.logSize + 256);
		for (int i = 0; i < this.logSize; i++) {
			if (this.numSensitive > 0 && i % interval == 0) {
				switch ((i / interval) % 3) {
					case 0:
						acOnly.append(" 手机号：133 ");
						break; // 非 11 位
					case 1:
						acOnly.append(" bank=[1234] ");
						break; // 恰好 4 位，前瞻不匹配
					default:
						acOnly.append(" {\"phone\":\"139\"} ");
						break; // 非 11 位
				}
			}
			else {
				acOnly.append('b');
			}
		}
		this.inputACOnly = acOnly.toString();

		// 构造无前缀命中的输入：包含类似值，但没有任何前缀关键词
		StringBuilder noPrefix = new StringBuilder(this.logSize + 128);
		for (int i = 0; i < this.logSize; i++) {
			if (this.numSensitive > 0 && i % interval == 0) {
				if ((i / interval) % 2 == 0) {
					noPrefix.append(" 13877891234 "); // 无前缀手机号形态
				}
				else {
					noPrefix.append(" [6222021234567890123] "); // 无前缀银行卡形态
				}
			}
			else {
				noPrefix.append('c');
			}
		}
		this.inputNoPrefix = noPrefix.toString();
	}

	@Benchmark
	public String sanitize() {
		return this.engine.desensitize(this.input);
	}

	@Benchmark
	public String sanitizeACOnly() {
		return this.engine.desensitize(this.inputACOnly);
	}

	@Benchmark
	public String sanitizeNoPrefix() {
		return this.engine.desensitize(this.inputNoPrefix);
	}

}
