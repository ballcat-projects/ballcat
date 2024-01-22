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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author <a href="mailto:cs.liaow@gmail.com">evil0th</a> Create on 2023/6/9
 */
public class RegexUtilsTest {

	@Test
	public void isPlate() {
		assertTrue(RegexUtils.match(RegexUtils.RE_PLATE_NUMBER, "京A13579"));
		assertTrue(RegexUtils.match(RegexUtils.RE_PLATE_NUMBER, "京AD13579"));
		assertTrue(RegexUtils.match(RegexUtils.RE_PLATE_NUMBER, "京AF13579"));
		assertTrue(RegexUtils.match(RegexUtils.RE_PLATE_NUMBER, "京A1357领"));
		assertTrue(RegexUtils.match(RegexUtils.RE_PLATE_NUMBER, "使000000"));
	}

	@Test
	public void isCitizenId() {
		assertTrue(RegexUtils.isCitizenId("430104198901066355"));
		assertFalse(RegexUtils.isCitizenId("11"));
	}

	@Test
	public void isSocialCreditCode() {
		assertTrue(RegexUtils.isSocialCreditCode("91110000101619988C"));
		assertFalse(RegexUtils.isSocialCreditCode("911100001016199888"));
	}

}
