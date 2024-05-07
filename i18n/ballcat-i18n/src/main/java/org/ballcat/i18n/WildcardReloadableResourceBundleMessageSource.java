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

package org.ballcat.i18n;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.util.ResourceUtils;

/**
 * 通配符支持的 ResourceBundleMessageSource，方便读取多个 jar 包中的资源文件.
 * <p>
 * 默认的 ReloadableResourceBundleMessageSource，对于多个同名文件，只会读取找到的第一个。
 *
 * @author Nicolás Miranda
 * @author hccake
 * @see <a href=
 * "https://stackoverflow.com/questions/3888832/does-spring-messagesource-support-multiple-class-path">Does
 * Spring MessageSource Support Multiple Class Path?</a>
 */
@Slf4j
public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	private static final String PROPERTIES_SUFFIX = ".properties";

	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private static final Pattern LOCALE_PROPERTIES_FILE_NAME_PATTERN = Pattern
		.compile(".*_([a-z]{2}(_[A-Z]{2})?(_[A-Z]+)?)\\.properties$");

	public WildcardReloadableResourceBundleMessageSource() {
		super.setResourceLoader(this.resolver);
	}

	/**
	 * Calculate all filenames for the given bundle basename and Locale. Will calculate
	 * filenames for the given Locale, the system Locale (if applicable), and the default
	 * file.
	 * @param basename the basename of the bundle
	 * @param locale the locale
	 * @return the List of filenames to check
	 * @see #setFallbackToSystemLocale
	 * @see #calculateFilenamesForLocale
	 */
	@Override
	protected List<String> calculateAllFilenames(String basename, Locale locale) {
		// 父类默认的方法会将 basename 也放入 filenames 列表
		List<String> filenames = super.calculateAllFilenames(basename, locale);
		// 当 basename 有匹配符时，从 filenames 中移除，否则扫描文件将抛出 Illegal char <*> 的异常
		if (containsWildcard(basename)) {
			filenames.remove(basename);
			try {
				Resource[] resources = getResources(basename);
				for (Resource resource : resources) {
					String resourceUriStr = resource.getURI().toString();
					// 根据通配符匹配到的多个 basename 对应的文件添加到文件列表末尾，作为兜底匹配
					if (!LOCALE_PROPERTIES_FILE_NAME_PATTERN.matcher(resourceUriStr).matches()) {
						String sourcePath = resourceUriStr.replace(PROPERTIES_SUFFIX, "");
						filenames.add(sourcePath);
					}
				}
			}
			catch (IOException ex) {
				log.error("读取国际化信息文件异常", ex);
			}
		}
		return filenames;
	}

	@Override
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		// 资源文件名
		List<String> fileNames = new ArrayList<>();
		// 获取到待匹配的国际化信息文件名集合
		List<String> matchFilenames = super.calculateFilenamesForLocale(basename, locale);
		for (String matchFilename : matchFilenames) {
			try {
				Resource[] resources = getResources(matchFilename);
				for (Resource resource : resources) {
					if (resource.exists()) {
						String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
						fileNames.add(sourcePath);
					}
				}
			}
			catch (IOException ex) {
				log.error("读取国际化信息文件异常", ex);
			}
		}
		return fileNames;
	}

	private Resource[] getResources(String resourceLocationPattern) throws IOException {
		// 支持用 . 表示文件层级
		resourceLocationPattern = resourceLocationPattern.replace(".", "/");
		return this.resolver.getResources("classpath*:" + resourceLocationPattern + PROPERTIES_SUFFIX);
	}

	private boolean containsWildcard(String str) {
		if (str.startsWith(ResourceUtils.CLASSPATH_URL_PREFIX)) {
			str = str.substring(ResourceUtils.CLASSPATH_URL_PREFIX.length());
		}
		return str.contains("*");
	}

}
