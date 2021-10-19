package com.hccake.starter.file;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2021/10/17 19:40
 */
@AllArgsConstructor
@EnableConfigurationProperties(FileProperties.class)
public class FileAutoConfiguration {

	@Bean
	public FileClient ballcatFileLocalClient(FileProperties properties) throws FileException {
		return new FileClient(properties);
	}

}
