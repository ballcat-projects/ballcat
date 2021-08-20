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
		if (basename.contains("*")) {
			filenames.remove(basename);
		}
		return filenames;
	}

	@Override
	protected List<String> calculateFilenamesForLocale(String basename, Locale locale) {
		// 支持 basename 用 . 表示文件层级
		basename = basename.replace(".", "/");

		// 资源文件名
		List<String> fileNames = new ArrayList<>();
		// 获取到待匹配的国际化信息文件名集合
		List<String> matchFilenames = super.calculateFilenamesForLocale(basename, locale);
		for (String matchFilename : matchFilenames) {
			try {
				Resource[] resources = resolver.getResources("classpath*:" + matchFilename + PROPERTIES_SUFFIX);
				for (Resource resource : resources) {
					String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
					fileNames.add(sourcePath);
				}
			}
			catch (IOException ignored) {
			}
		}
		return fileNames;
	}

}
