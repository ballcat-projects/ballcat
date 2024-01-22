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

package org.ballcat.tesseract;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import org.ballcat.tesseract.exception.OcrException;

/**
 * <p>
 * TesseractCommand class.
 * </p>
 *
 * @author lingting
 */
@Getter
@Builder
public class TesseractCommand {

	private String tesseract;

	private TesseractImage image;

	private String lang;

	private boolean boxes;

	/**
	 * 页面分割模式. 值范围 1-13; 默认 3
	 * <p>
	 * 小文本可以调大.
	 */
	private Integer psm;

	/**
	 * <p>
	 * getCommand.
	 * </p>
	 * @return a {@link java.lang.String} object.
	 */
	public String getCommand() {
		StringBuilder builder = new StringBuilder("\"").append(this.tesseract).append("\" \"");

		try {
			builder.append(this.image.write()).append("\" stdout");
		}
		catch (IOException e) {
			throw new OcrException("图片加载异常!", e);
		}

		if (hasText(this.lang)) {
			builder.append(" -l ").append(this.lang);
		}

		if (this.psm != null) {
			builder.append(" --psm ").append(this.psm);
		}

		if (this.boxes) {
			builder.append(" makebox");
		}

		return builder.toString();
	}

	/**
	 * <p>
	 * run.
	 * </p>
	 * @return a {@link java.util.List} object.
	 */
	public List<String> run() {
		try {
			final Process process = Runtime.getRuntime().exec(getCommand());
			final InputStream in = process.getInputStream();

			final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			List<String> lines = new ArrayList<>();
			String line;

			while ((line = reader.readLine()) != null) {
				if (hasText(line)) {
					lines.add(line);
				}
			}

			process.waitFor();
			in.close();
			reader.close();
			process.destroy();
			return lines;
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new OcrException("识别被中断!", e);
		}
		catch (Exception e) {
			throw new OcrException("识别异常!", e);
		}
	}

	/**
	 * <p>
	 * hasText.
	 * </p>
	 * @param str a {@link java.lang.String} object.
	 * @return a boolean.
	 */
	protected boolean hasText(String str) {
		if (str == null || str.length() == 0) {
			return false;
		}

		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return true;
			}
		}

		return false;
	}

}
