package com.hccake.starter.file;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lingting 2021/10/17 19:37
 */
@Data
@ConfigurationProperties(prefix = "ballcat.file")
public class FileProperties {

	/**
	 * <h1>本地文件存放原始路径</h1>
	 * <p>
	 * 如果为Null, 默认存放在系统临时目录下
	 * </p>
	 */
	private String path;

}
