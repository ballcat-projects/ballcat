package com.hccake.starter.file;

import com.hccake.starter.file.ftp.FtpMode;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/10/17 19:37
 */
@Data
@ConfigurationProperties(prefix = "ballcat.file")
public class FileProperties {

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

	}

}
