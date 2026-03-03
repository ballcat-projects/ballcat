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

import java.util.Collections;
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
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode({ Mode.AverageTime })
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class BankMaskBenchmarks {

	@Param({ "128", "512", "1024" })
	public int payloadKB; // 负载大小

	@Param({ "0", "1", "4", "16" })
	public int hitsPerKB; // 每KB命中次数

	@Param({ "6222021234567890123", "6222021234567890" })
	public String bank; // 19位/16位

	private TextDesensitizer slideEngine;

	private TextDesensitizer regexEngine;

	private String payloadHit; // 有命中

	@Setup
	public void setup() {
		// slide 引擎
		SlideMaskParams slide = new SlideMaskParams();
		slide.setLeftPlainTextLen(0);
		slide.setRightPlainTextLen(4);
		slide.setMaskString("*");
		RuleSpec slideRule = new RuleSpec().setName("bank-slide")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("\\d{16,19}")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.SLIDE_MASK)
			.setSlide(slide);
		this.slideEngine = new TextDesensitizerBuilder().addRule(slideRule).build();

		// regex 引擎
		RegexReplacementParams regex = new RegexReplacementParams().setReplacement("****$1");
		RuleSpec regexRule = new RuleSpec().setName("bank-regex")
			.setPrefixes(Collections.singletonList("bank"))
			.setValuePattern("(\\d{12,15})(\\d{4})")
			.setMatchFromStart(true)
			.setDesensitizeType(DesensitizeType.REGEX_REPLACEMENT)
			.setRegex(regex);
		this.regexEngine = new TextDesensitizerBuilder().addRule(regexRule).build();

		buildPayloads();
	}

	private void buildPayloads() {
		int total = this.payloadKB * 1024;
		int hits = this.hitsPerKB * this.payloadKB;
		String tokenHit = " bank=[" + this.bank + "] ";
		int avgGap = hits > 0 ? Math.max(1, (total - hits * tokenHit.length()) / hits) : total;

		StringBuilder hit = new StringBuilder(total + 128);
		for (int i = 0; i < hits; i++) {
			for (int j = 0; j < avgGap; j++) {
				hit.append('a');
			}
			hit.append(tokenHit);

		}
		while (hit.length() < total) {
			hit.append('x');
		}
		this.payloadHit = hit.toString();
	}

	// 命中：比较两种实现
	@Benchmark
	public String slide_hit() {
		return this.slideEngine.desensitize(this.payloadHit);
	}

	@Benchmark
	public String regex_hit() {
		return this.regexEngine.desensitize(this.payloadHit);
	}

}
