package com.ballcat.startes.oss.test;

import com.hccake.ballcat.common.oss.domain.StreamTemp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author lingting
 */
class StreamTempTest {

	@Test
	void of() throws IOException {
		final File file = new File("C:\\Users\\lingting\\Pictures\\rgb.jpg");
		final StreamTemp temp = StreamTemp.of(new FileInputStream(file));
		Assertions.assertEquals(47558, temp.getSize());
		Throwable throwable = null;
		try {
			temp.getStream().close();
		}
		catch (Throwable t) {
			throwable = t;
		}
		Assertions.assertNull(throwable);
	}

}
