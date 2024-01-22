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

import java.io.IOException;

import lombok.AllArgsConstructor;
import org.ballcat.file.core.FileClient;
import org.ballcat.file.ftp.FtpFileClient;
import org.ballcat.file.local.LocalFileClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

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
	public FileClient ballcatFileFtpClient(FileProperties properties) throws IOException {
		return new FtpFileClient(properties.getFtp());
	}

	@Bean
	@ConditionalOnMissingBean(FileClient.class)
	public FileClient ballcatFileLocalClient(FileProperties properties) throws IOException {
		FileProperties.LocalProperties localProperties = properties == null || properties.getLocal() == null
				? new FileProperties.LocalProperties() : properties.getLocal();
		return new LocalFileClient(localProperties);
	}

}
