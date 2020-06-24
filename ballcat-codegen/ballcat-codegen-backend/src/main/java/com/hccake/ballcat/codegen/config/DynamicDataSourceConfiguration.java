package com.hccake.ballcat.codegen.config;

import com.baomidou.dynamic.datasource.processor.DsHeaderProcessor;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.processor.DsSessionProcessor;
import com.baomidou.dynamic.datasource.processor.DsSpelExpressionProcessor;
import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/15 17:37 动态数据源加载
 */
@Configuration
@RequiredArgsConstructor
public class DynamicDataSourceConfiguration {

	private final StringEncryptor stringEncryptor;

	/**
	 * 默认JDBC驱动类
	 */
	public static final String DEFAULT_JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";

	/**
	 * JDBC 动态数据源提供者
	 * @param dataSourceProperties spring数据源配置
	 * @return DynamicJdbcDataSourceProvider 动态数据源提供者
	 */
	@Bean
	public DynamicJdbcDataSourceProvider dynamicDataSourceProvider(DataSourceProperties dataSourceProperties) {
		// 这里由于 dynamic-datasource-spring-boot-starter 的问题，必须填写
		String driverClassName = dataSourceProperties.getDriverClassName();
		if (driverClassName == null) {
			driverClassName = DEFAULT_JDBC_DRIVER;
		}
		return new DynamicJdbcDataSourceProvider(stringEncryptor, driverClassName, dataSourceProperties.getUrl(),
				dataSourceProperties.getUsername(), dataSourceProperties.getPassword());
	}

	@Bean
	public DsProcessor dsProcessor() {
		DsRequestProcessor requestProcessor = new DsRequestProcessor();
		DsHeaderProcessor headerProcessor = new DsHeaderProcessor();
		DsSessionProcessor sessionProcessor = new DsSessionProcessor();
		DsSpelExpressionProcessor spelExpressionProcessor = new DsSpelExpressionProcessor();
		requestProcessor.setNextProcessor(headerProcessor);
		headerProcessor.setNextProcessor(sessionProcessor);
		sessionProcessor.setNextProcessor(spelExpressionProcessor);
		return requestProcessor;
	}

}
