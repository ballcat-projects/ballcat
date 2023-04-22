package com.hccake.ballcat.common.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

/**
 * @author lingting
 */
class FileUtilsTest {

	@Test
	void getTemplate() throws IOException {
		final File file = FileUtils.createTemp("jjk");
		Assertions.assertNotNull(file);
	}

}
