package com.hccake.starter.file;

import com.hccake.starter.file.FileProperties.LocalProperties;
import com.hccake.starter.file.core.FileClient;
import com.hccake.starter.file.ftp.FtpFileClient;
import com.hccake.starter.file.local.LocalFileClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author lingting 2021/10/17 19:40
 * @author 疯狂的狮子Li 2022-04-24
 */
@AutoConfiguration
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	@ConditionalOnProperty(prefix = FileProperties.PREFIX_FTP, name = "ip")
	public FileClient ballcatFileFtpClient(FileProperties properties) {
		return new FtpFileClient(properties.getFtp());
	}

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	public FileClient ballcatFileLocalClient(FileProperties properties) throws IOException {
		LocalProperties localProperties = properties == null || properties.getLocal() == null ? new LocalProperties()
				: properties.getLocal();
		return new LocalFileClient(localProperties);
	}

}
