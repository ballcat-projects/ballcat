package com.hccake.common.i18n;

import com.hccake.common.i18n.cache.CacheService;
import com.hccake.common.i18n.cache.LocalCacheService;
import com.hccake.common.i18n.cache.RedisCacheService;
import com.hccake.common.i18n.execute.DefaultTranslateExecute;
import com.hccake.common.i18n.execute.TranslateExecute;
import com.hccake.common.i18n.execute.TranslateExecuteWrapper;
import com.hccake.common.i18n.executor.CacheExecutor;
import com.hccake.common.i18n.executor.Executor;
import com.hccake.common.i18n.executor.ExecutorWrapper;
import com.hccake.common.i18n.executor.SimpleExecutor;
import com.hccake.common.i18n.generate.DefaultKeyGenerate;
import com.hccake.common.i18n.generate.KeyGenerate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * i18n auto configuration
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {

	private final I18nProperties i18nProperties;

	private final I18nDataProvider i18nDataProvider;

	@Bean
	@ConditionalOnMissingBean
	public KeyGenerate keyGenerate() {
		return new DefaultKeyGenerate(i18nProperties);
	}

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = "ballcat.i18n.cache", name = "type", havingValue = "local", matchIfMissing = true)
	public CacheService localCacheService() {
		return new LocalCacheService();
	}

	@Bean
	@ConditionalOnMissingBean
	@DependsOn("redisTemplate")
	@ConditionalOnProperty(prefix = "ballcat.i18n.cache", name = "type", havingValue = "redis")
	public CacheService redisCacheService(StringRedisTemplate stringRedisTemplate) {
		return new RedisCacheService(stringRedisTemplate);
	}

	@Bean
	@ConditionalOnMissingBean(Executor.class)
	@ConditionalOnProperty(prefix = "ballcat.i18n", name = "executor", havingValue = "simple", matchIfMissing = true)
	public Executor simpleExecutor() {
		return new SimpleExecutor(i18nDataProvider);
	}

	@Bean
	@ConditionalOnBean(CacheService.class)
	@ConditionalOnMissingBean(Executor.class)
	@ConditionalOnProperty(prefix = "ballcat.i18n", name = "executor", havingValue = "cache")
	public Executor cacheExecutor(KeyGenerate keyGenerate, CacheService cacheService) {
		return new CacheExecutor(i18nDataProvider, i18nProperties, keyGenerate, cacheService);
	}

	@Bean
	@ConditionalOnBean(Executor.class)
	@ConditionalOnMissingBean
	public TranslateExecute translateExecute(Executor executor) {
		return new DefaultTranslateExecute(new ExecutorWrapper(i18nProperties, executor));
	}

	@Bean
	@ConditionalOnBean({ Executor.class, TranslateExecute.class })
	@ConditionalOnMissingBean
	public TranslateExecuteWrapper translateExecuteWrapper(TranslateExecute translateExecute) {
		TranslateExecuteWrapper translateExecuteWrapper = new TranslateExecuteWrapper();
		translateExecuteWrapper.setTranslateExecute(translateExecute);
		return translateExecuteWrapper;
	}

}
