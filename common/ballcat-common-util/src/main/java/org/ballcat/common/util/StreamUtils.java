/*
 * Copyright 2023 the original author or authors.
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

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * @author lingting 2021/4/21 17:45
 */
@UtilityClass
public class StreamUtils {

	/**
	 * 默认大小 1024 * 1024 * 8
	 */
	public final int DEFAULT_SIZE = 10485760;

	public byte[] read(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		write(in, out);
		try {
			return out.toByteArray();
		}
		finally {
			close(out);
		}
	}

	public void write(InputStream in, OutputStream out) throws IOException {
		write(in, out, DEFAULT_SIZE);
	}

	public void write(InputStream in, OutputStream out, int size) throws IOException {
		byte[] bytes = new byte[size];
		int len;

		while (true) {
			len = in.read(bytes);
			if (len <= 0) {
				break;
			}

			out.write(bytes, 0, len);
		}
	}

	public String toString(InputStream in) throws IOException {
		return toString(in, DEFAULT_SIZE, StandardCharsets.UTF_8);
	}

	public String toString(InputStream in, int size, Charset charset) throws IOException {
		byte[] bytes = new byte[size];
		int len;

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

		while (true) {
			len = in.read(bytes);
			if (len <= 0) {
				break;
			}
			outputStream.write(bytes, 0, len);
		}

		return outputStream.toString(charset.name());
	}

	/**
	 * 从流中读取 int
	 */
	public int readInt(InputStream is, int noOfBytes, boolean bigEndian) throws IOException {
		int ret = 0;
		int sv = bigEndian ? ((noOfBytes - 1) * 8) : 0;
		int cnt = bigEndian ? -8 : 8;
		for (int i = 0; i < noOfBytes; i++) {
			ret |= is.read() << sv;
			sv += cnt;
		}
		return ret;
	}

	public void close(Closeable closeable) {
		try {
			if (closeable != null) {
				closeable.close();
			}
		}
		catch (Exception e) {
			//
		}
	}

	/**
	 * 克隆文件流
	 * <p color="red">
	 * 注意: 在使用后及时关闭复制流
	 * </p>
	 * @param stream 源流
	 * @param amounts 数量
	 * @return 返回指定数量的从源流复制出来的只读流
	 */
	public InputStream[] clone(InputStream stream, Integer amounts) throws IOException {
		return clone(stream, amounts, DEFAULT_SIZE);
	}

	public InputStream[] clone(InputStream stream, Integer amounts, int size) throws IOException {
		InputStream[] streams = new InputStream[amounts];
		File[] files = new File[amounts];
		FileOutputStream[] outs = new FileOutputStream[amounts];

		byte[] buffer = new byte[size < 1 ? DEFAULT_SIZE : size];
		int len;

		while ((len = stream.read(buffer)) > -1) {
			for (int i = 0, outsLength = outs.length; i < outsLength; i++) {
				FileOutputStream out = outs[i];
				if (out == null) {
					files[i] = FileUtils.createTemp("clone." + i + "." + System.currentTimeMillis());
					out = new FileOutputStream(files[i]);
					outs[i] = out;
				}
				out.write(buffer, 0, len);
			}
		}

		for (int i = 0; i < files.length; i++) {
			try {
				outs[i].close();
			}
			catch (IOException e) {
				//
			}

			streams[i] = Files.newInputStream(files[i].toPath());
		}

		return streams;
	}

}
