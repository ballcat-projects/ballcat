package com.hccake.extend.mybatis.plus;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.hccake.extend.mybatis.plus.config.MybatisConfigurer;
import com.hccake.extend.mybatis.plus.config.MybatisProperties;
import com.hccake.extend.mybatis.plus.config.StaticConfig;
import com.hccake.extend.mybatis.plus.exception.MybatisMethodInstanceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lingting  2020/6/9 19:59
 */
@Slf4j
@ComponentScan
@Configuration
@EnableConfigurationProperties({MybatisProperties.class})
public class MybatisAutoConfiguration {
	private final List<MybatisConfigurer> configurers;
	private final MybatisProperties properties;

	public MybatisAutoConfiguration(List<MybatisConfigurer> configurers, MybatisProperties properties) {
		this.configurers = configurers;
		this.properties = properties;

		configurers.forEach(mybatisConfigurer -> {
			StaticConfig.UPDATE_IGNORE_FIELDS.addAll(properties.getIgnoreFields());
			mybatisConfigurer.addIgnoreFields(StaticConfig.UPDATE_IGNORE_FIELDS);
		});
	}

	@Bean
	public SqlInjector sqlInjector() {
		List<AbstractMethod> list = new ArrayList<>();

		for (Class<? extends AbstractMethod> c : properties.getMethods()) {
			try {
				list.add(c.newInstance());
			} catch (InstantiationException | IllegalAccessException e) {
				throw new MybatisMethodInstanceException("获取自定义全局方法实例出错 class: " + c.getName(), e);
			}
		}

		configurers.forEach(mybatisConfigurer -> mybatisConfigurer.addGlobalMethods(list));
		return new SqlInjector(list);
	}
}
