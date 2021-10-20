package com.hccake.starter.file;

import com.hccake.starter.file.FileProperties.LocalProperties;
import com.hccake.starter.file.ftp.FtpClient;
import com.hccake.starter.file.local.FileLocalClient;
import com.hccake.starter.file.local.FileLocalException;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/10/17 19:40
 */
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	@ConditionalOnProperty(prefix = "ballcat.ftp", name = "ip")
	public FileClient ballcatFileFtpClient(FileProperties properties) throws IOException {
		return new FtpClient(properties.getFtp());
	}

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	public FileClient ballcatFileLocalClient(FileProperties properties) throws FileLocalException {
		LocalProperties localProperties = properties == null || properties.getLocal() == null ? new LocalProperties()
				: properties.getLocal();
		return new FileLocalClient(localProperties);
	}

}
