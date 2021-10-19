package com.hccake.starter.file;

import com.hccake.ballcat.common.oss.OssClient;
import com.hccake.starter.ftp.FtpClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/10/17 19:40
 */
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean({OssClient.class, FtpClient.class})
	public FileClient ballcatFileLocalClient(FileProperties properties) throws FileException {
		return new FileClient(properties);
	}
}
