package com.hccake.starter.ftp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/10/18 9:54
 */
@Data
@ConfigurationProperties(prefix = "ballcat.ftp")
public class FtpProperties {

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
