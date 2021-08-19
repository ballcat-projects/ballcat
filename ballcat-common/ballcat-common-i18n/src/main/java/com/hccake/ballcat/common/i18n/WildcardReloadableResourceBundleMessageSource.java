package com.hccake.ballcat.common.i18n;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * 通配符支持的 ResourceBundleMessageSource，方便读取多个 jar 包中的资源文件.
 *
 * 默认的 ReloadableResourceBundleMessageSource，对于多个同名文件，只会读取找到的第一个。
 *
 * @see <a href=
 * "https://stackoverflow.com/questions/3888832/does-spring-messagesource-support-multiple-class-path">Does
 * Spring MessageSource Support Multiple Class Path?</a>
 * @author Nicolás Miranda
 * @author hccake
 */
public class WildcardReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

	private static final String PROPERTIES_SUFFIX = ".properties";

	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@Override
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		basename = basename.replace(".", "/");
		List<String> filenames = super.calculateFilenamesForLocale(basename, locale);
		List<String> add = new ArrayList<>();
		for (String filename : filenames) {
			try {
				Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
				for (Resource resource : resources) {
					String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
					add.add(sourcePath);
				}
			}
			catch (IOException ignored) {
			}
		}
		filenames.addAll(add);
		return filenames;
	}

}
