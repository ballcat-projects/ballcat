package com.hccake.ballcat.common.system;

import com.hccake.ballcat.common.util.StreamUtils;
import lombok.Getter;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.LocalDateTime;

/**
 * @author lingting 2022/6/25 12:01
 */
public class CommandResult {

	protected File stdOut;

	protected File stdErr;

	private Charset charset;

	@Getter
	protected LocalDateTime startTime;

	@Getter
	protected LocalDateTime endTime;

	protected String strOutput = null;

	protected String strError = null;

	public static CommandResult of(File stdOut, File stdErr, LocalDateTime startTime, LocalDateTime endTime,
			Charset charset) {
		CommandResult result = new CommandResult();
		result.stdOut = stdOut;
		result.stdErr = stdErr;
		result.charset = charset;
		result.startTime = startTime;
		result.endTime = endTime;
		return result;
	}

	public File stdOut() {
		return stdOut;
	}

	public File stdErr() {
		return stdErr;
	}

	public String stdOutStr() throws IOException {
		if (!StringUtils.hasText(strOutput)) {
			try (FileInputStream output = new FileInputStream(stdOut)) {
				strOutput = StreamUtils.toString(output, StreamUtils.DEFAULT_SIZE, charset);
			}
		}
		return strOutput;
	}

	public String stdErrStr() throws IOException {
		if (!StringUtils.hasText(strError)) {
			try (FileInputStream error = new FileInputStream(stdErr)) {
				strError = StreamUtils.toString(error, StreamUtils.DEFAULT_SIZE, charset);
			}
		}
		return strError;
	}

	public InputStream stdOutStream() throws IOException {
		return Files.newInputStream(stdOut.toPath());
	}

	public InputStream stdErrStream() throws IOException {
		return Files.newInputStream(stdErr.toPath());
	}

	public void clean() {
		try {
			Files.delete(stdOut.toPath());
		}
		catch (Exception e) {
			//
		}
		try {
			Files.delete(stdErr.toPath());
		}
		catch (Exception e) {
			//
		}
	}

}
