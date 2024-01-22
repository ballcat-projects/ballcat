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

package org.ballcat.common.system;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.ballcat.common.exception.CommandTimeoutException;
import org.ballcat.common.util.FileUtils;
import org.ballcat.common.util.SystemUtils;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2022/6/25 11:55
 */
public final class Command {

	public static final String NEXT_LINE = SystemUtils.lineSeparator();

	public static final String EXIT_COMMAND = "exit";

	private final Process process;

	private final OutputStream stdIn;

	/**
	 * 标准输出
	 */
	private final File stdOut;

	private final File stdErr;

	private final String nextLine;

	private final String exit;

	private final Charset charset;

	private final LocalDateTime startTime;

	private Command(String init, String nextLine, String exit, Charset charset) throws IOException {
		if (!StringUtils.hasText(init)) {
			throw new IllegalArgumentException("Empty init");
		}
		StringTokenizer st = new StringTokenizer(init);
		String[] cmdArray = new String[st.countTokens()];
		for (int i = 0; st.hasMoreTokens(); i++) {
			cmdArray[i] = st.nextToken();
		}

		this.stdOut = FileUtils.createTemp();
		this.stdErr = FileUtils.createTemp();

		// 重定向标准输出和标准错误到文件, 避免写入到缓冲区然后占满导致 waitFor 死锁
		ProcessBuilder builder = new ProcessBuilder(cmdArray).redirectError(this.stdErr).redirectOutput(this.stdOut);
		this.process = builder.start();
		this.stdIn = this.process.getOutputStream();
		this.nextLine = nextLine;
		this.exit = exit;
		this.charset = charset;
		this.startTime = LocalDateTime.now();
	}

	/**
	 * 获取命令操作实例. 此实例默认使用系统字符集, 如果发现部分带非英文字符和特殊符号命令执行异常, 建议使用
	 * {@link Command#of(String, Charset)} 自定义对应的字符集
	 * @param init 初始命令
	 */
	public static Command of(String init) throws IOException {
		return of(init, SystemUtils.charset());
	}

	/**
	 * 推荐使用此实例
	 */
	public static Command of(String init, Charset charset) throws IOException {
		return of(init, NEXT_LINE, EXIT_COMMAND, charset);
	}

	public static Command of(String init, String nextLine, String exit, Charset charset) throws IOException {
		return new Command(init, nextLine, exit, charset);
	}

	public Command write(String str) throws IOException {
		this.stdIn.write(str.getBytes(this.charset));
		this.stdIn.flush();
		return this;
	}

	/**
	 * 换到下一行
	 */
	public Command line() throws IOException {
		return write(this.nextLine);
	}

	/**
	 * 写入通道退出指令
	 */
	public Command exit() throws IOException {
		write(this.exit);
		return line();
	}

	/**
	 * 写入并执行一行指令
	 * @param str 单行指令
	 */
	public Command exec(String str) throws IOException {
		write(str);
		return line();
	}

	/**
	 * 获取执行结果, 并退出
	 * <p>
	 * 注意: 如果套娃了多个通道, 则需要手动退出套娃的通道
	 * </p>
	 * <p>
	 * 例如: eg: exec("ssh ssh.lingting.live").exec("ssh ssh.lingting.live").exec("ssh
	 * ssh.lingting.live")
	 * </p>
	 * <p>
	 * 需要: eg: exit().exit().exit()
	 * </p>
	 */
	public CommandResult result() throws InterruptedException {
		this.process.waitFor();
		return CommandResult.of(this.stdOut, this.stdErr, this.startTime, LocalDateTime.now(), this.charset);
	}

	/**
	 * 等待命令执行完成
	 * <h3>如果 process 是通过 {@link Runtime#exec}方法构建的, 那么{@link Process#waitFor}方法可能会导致线程卡死,
	 * 具体原因如下</h3>
	 * <p>
	 * 终端缓冲区大小有限, 在缓冲区被写满之后, 会子线程会挂起,等待缓冲区内容被读, 然后才继续写. 如果此时主线程也在waitFor()等待子线程结束, 就卡死了
	 * </p>
	 * <p>
	 * 即便是先读取返回结果在调用此方法也可能会导致卡死. 比如: 先读取标准输出流, 还没读完, 缓冲区被错误输出流写满了.
	 * </p>
	 * @param millis 等待时间, 单位: 毫秒
	 * @return live.lingting.tools.system.CommandResult
	 */
	public CommandResult result(long millis) throws InterruptedException, CommandTimeoutException {
		if (this.process.waitFor(millis, TimeUnit.MILLISECONDS)) {
			return result();
		}
		// 超时. 强行杀死子线程
		this.process.destroyForcibly();
		throw new CommandTimeoutException();
	}

	public void close() {
		this.process.destroy();
	}

}
