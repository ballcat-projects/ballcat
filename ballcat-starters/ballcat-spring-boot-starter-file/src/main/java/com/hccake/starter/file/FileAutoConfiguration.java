package com.hccake.starter.file;

import com.hccake.starter.file.FileProperties.LocalProperties;
import com.hccake.starter.file.ftp.FtpFileClient;
import com.hccake.starter.file.local.LocalFileClient;
import com.hccake.starter.file.local.LocalFileException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.io.IOException;

/**
 * @author lingting 2021/10/17 19:40
 */
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	@ConditionalOnProperty(prefix = FileProperties.PREFIX_FTP, name = "ip")
	public FileClient ballcatFileFtpClient(FileProperties properties) throws IOException {
		return new FtpFileClient(properties.getFtp());
	}

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	public FileClient ballcatFileLocalClient(FileProperties properties) throws LocalFileException {
		LocalProperties localProperties = properties == null || properties.getLocal() == null ? new LocalProperties()
				: properties.getLocal();
		return new LocalFileClient(localProperties);
	}

}
