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

package org.ballcat.oss;

import org.ballcat.oss.prefix.ObjectKeyPrefixConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 抽象OSS操作测试模板
 *
 * @author lishangbu 2022/10/27
 */
@SpringBootTest(classes = OssAutoConfiguration.class)
public abstract class AbstractOssTemplateTest {

	@Autowired
	protected OssTemplate ossTemplate;

	@Autowired
	protected ObjectKeyPrefixConverter objectKeyPrefixConverter;

	protected void createBucket(String bucket) {
		this.ossTemplate.createBucket(bucket);
	}

	protected void deleteBucket(String bucket) {
		this.ossTemplate.deleteBucket(bucket);
	}

}
