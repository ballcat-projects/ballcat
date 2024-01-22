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

package org.ballcat.file;

import lombok.Data;
import org.ballcat.file.ftp.FtpMode;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/10/17 19:37
 */
@Data
@ConfigurationProperties(prefix = FileProperties.PREFIX)
public class FileProperties {

	public static final String PREFIX = "ballcat.file";

	public static final String PREFIX_FTP = PREFIX + ".ftp";

	public static final String PREFIX_LOCAL = PREFIX + ".local";

	private LocalProperties local;

	private FtpProperties ftp;

	@Data
	public static class LocalProperties {

		/**
		 * <h1>本地文件存放原始路径</h1>
		 * <p>
		 * 如果为Null, 默认存放在系统临时目录下
		 * </p>
		 */
		private String path;

	}

	@Data
	public static class FtpProperties {

		/**
		 * 目标ip
		 */
		private String ip;

		/**
		 * ftp端口
		 */
		private Integer port = 21;

		/**
		 * 用户名
		 */
		private String username;

		/**
		 * 密码
		 */
		private String password;

		/**
		 * ftp路径
		 */
		private String path = "/";

		/**
		 * 模式
		 */
		private FtpMode mode;

		/**
		 * 字符集
		 */
		private String encoding = "UTF-8";

		/**
		 * sftp设置true
		 */
		private boolean sftp;

	}

}
