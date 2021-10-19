package com.hccake.starter.ftp;

import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/10/18 9:55
 */
@AllArgsConstructor
@EnableConfigurationProperties(FtpProperties.class)
public class FtpAutoConfiguration {

	@Bean
	@ConditionalOnProperty(prefix = "ballcat.ftp", name = "ip")
	public FtpClient ballcatFtpClient(FtpProperties properties) throws IOException {
		return new FtpClient(properties);
	}

}
