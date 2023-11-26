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