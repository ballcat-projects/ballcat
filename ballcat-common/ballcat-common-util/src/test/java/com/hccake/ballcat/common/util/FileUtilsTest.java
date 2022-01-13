package com.hccake.ballcat.common.util;

import java.io.File;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting
 */
class FileUtilsTest {

	@Test
	void getTemplate() throws IOException {
		final File file = FileUtils.getTemplateFile("jjk");
		Assertions.assertNotNull(file);
	}

}
