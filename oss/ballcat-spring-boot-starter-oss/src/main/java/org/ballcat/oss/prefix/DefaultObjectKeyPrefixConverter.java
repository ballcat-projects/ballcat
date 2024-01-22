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

package org.ballcat.oss.prefix;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.oss.OssConstants;
import org.ballcat.oss.OssProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 默认对象前缀处理器
 *
 * @author lishangbu 2022/10/23
 */
@Slf4j
@RequiredArgsConstructor
public class DefaultObjectKeyPrefixConverter implements ObjectKeyPrefixConverter, InitializingBean {

	/**
	 * OSS属性配置
	 */
	private final OssProperties properties;

	/**
	 * 全局对象前缀
	 */
	private String globalObjectPrefix;

	@Override
	public String getPrefix() {
		return this.properties.getObjectKeyPrefix();
	}

	/**
	 * 判断是否匹配该前缀处理器 当objectKeyPrefix配置非法时,globalObjectPrefix可能不会生效
	 * @return true 匹配 false不匹配
	 */
	@Override
	public boolean match() {
		return StringUtils.hasText(getPrefix()) && StringUtils.hasText(this.globalObjectPrefix);
	}

	@Override
	public String unwrap(String key) {
		return match() && key.startsWith(this.globalObjectPrefix) ? key.substring(this.globalObjectPrefix.length())
				: key;
	}

	@Override
	public String wrap(String key) {
		return match() ? this.globalObjectPrefix + key : key;
	}

	/**
	 * 输出当前的路径标准化对象前缀信息 参考官方文档<a href=
	 * "https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/using-prefixes.html">使用前缀组织对象</a>
	 */
	@Override
	public void afterPropertiesSet() {
		String configPrefix = getPrefix();
		if (ObjectUtils.isEmpty(configPrefix)) {
			log.info("未配置全局前缀配置,全局前缀组织对象功能不启用");
			return;
		}
		if (OssConstants.SLASH.equals(configPrefix)) {
			log.warn("全局前缀路径为非法配置:[{}],全局路径不起效,全局前缀组织对象功能不启用", OssConstants.SLASH);
			return;
		}

		this.globalObjectPrefix = configPrefix;
		// 保证 全局前缀路径 不以 / 开头
		if (this.globalObjectPrefix.startsWith(OssConstants.SLASH)) {
			this.globalObjectPrefix = this.globalObjectPrefix.substring(1);
		}

		// 保证 全局前缀路径 以 / 结尾
		if (!this.globalObjectPrefix.endsWith(OssConstants.SLASH)) {
			this.globalObjectPrefix = this.globalObjectPrefix + OssConstants.SLASH;
		}

		if (log.isInfoEnabled()) {
			log.info("全局前缀组织对象功能启用，全局前缀配置路径为:[{}],标准化前缀全局前缀路径为:[{}]", configPrefix, this.globalObjectPrefix);
			log.info("存在全局前缀时，针对用户操作OSS对象(上传、删除)的部分会自动拼接全局前缀，针对用户读取OSS对象的部分，返回的OSS对象会自动移除全局前缀,但实际存储在OSS的位置也会包含全局路径");
			log.info("例如,存在`abc`桶时，全局前缀设置为`d`时,上传OSS对象`e.txt`时,OSS对象会按照`d/e.txt`保存，用户在具有权限时可通过资源`{}/abc/d/e.txt`访问该资源",
					this.properties.getEndpoint());
			log.info("用户试图查找该资源时，只需要传入`e.txt`,插件会自动发起对`d/e.txt`路径的查询,返回的OSS对象也会去除查询到的对象名称将为`e.txt`");
			log.info("用户试图删除该资源时，只需要传入`e.txt`,插件会自动发起对`d/e.txt`路径对象的删除");
		}
	}

}
