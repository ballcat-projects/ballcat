package com.hccake.ballcat.commom.storage.domain;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author lingting 2021/5/10 15:34
 */
@Data
@Accessors(chain = true)
@RequiredArgsConstructor
public class StreamTemp {

	private final Long size;

	private final InputStream stream;

	public static StreamTemp of(InputStream stream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		long size = 0;
		byte[] buffer = new byte[1024];
		int len;

		while ((len = stream.read(buffer)) > -1) {
			size += len;
			out.write(buffer, 0, len);
		}

		return new StreamTemp(size, new ByteArrayInputStream(out.toByteArray()));
	}

}
