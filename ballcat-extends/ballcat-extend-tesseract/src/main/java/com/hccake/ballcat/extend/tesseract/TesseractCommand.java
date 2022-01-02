package com.hccake.ballcat.extend.tesseract;

import com.hccake.ballcat.extend.tesseract.exception.OcrException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;

/**
 * @author lingting
 */
@Builder
public class TesseractCommand {

	private String tesseract;

	private TesseractImage image;

	private String lang;

	private boolean boxes;

	public List<String> run() {
		StringBuilder builder = new StringBuilder("\"").append(tesseract).append("\" \"");

		try {
			builder.append(image.write()).append("\" stdout");
		}
		catch (IOException e) {
			throw new OcrException("图片加载异常!", e);
		}

		if (hasText(lang)) {
			builder.append(" -l ").append(lang);
		}

		if (boxes) {
			builder.append(" makebox");
		}

		return run(builder.toString());
	}

	private List<String> run(String command) {
		try {
			final Process process = Runtime.getRuntime().exec(command);
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

	boolean hasText(String str) {
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
