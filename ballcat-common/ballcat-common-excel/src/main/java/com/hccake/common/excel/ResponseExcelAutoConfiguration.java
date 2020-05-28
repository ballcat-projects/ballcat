package com.hccake.common.excel;

import com.hccake.common.excel.aop.DynamicNameAspect;
import com.hccake.common.excel.aop.ResponseExcelReturnValueHandler;
import com.hccake.common.excel.config.ExcelConfigProperties;
import com.hccake.common.excel.handler.ManySheetWriteHandler;
import com.hccake.common.excel.handler.SheetWriteHandler;
import com.hccake.common.excel.handler.SingleSheetWriteHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import com.hccake.common.excel.processor.NameProcessor;
import com.hccake.common.excel.processor.NameSpelExpressionProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lengleng
 * @date 2020/3/29
 * <p>
 * 配置初始化
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(ExcelConfigProperties.class)
public class ResponseExcelAutoConfiguration implements ApplicationContextAware, InitializingBean {
	private ApplicationContext applicationContext;
	private final List<SheetWriteHandler> sheetWriteHandlerList;


	/**
	 * SPEL 解析处理器
	 * @return  NameProcessor excle名称解析器
	 */
	@Bean
	@ConditionalOnMissingBean
	public NameProcessor nameProcessor() {
		return new NameSpelExpressionProcessor();
	}

	/**
	 * Excle名称解析处理切面
	 * @param nameProcessor  SPEL 解析处理器
	 * @return DynamicNameAspect
	 */
	@Bean
	@ConditionalOnMissingBean
	public DynamicNameAspect dynamicNameAspect(NameProcessor nameProcessor) {
		return new DynamicNameAspect(nameProcessor);
	}

	/**
	 * 多sheet处理器
	 * @param configProperties Excle导出配置
	 * @return ManySheetWriteHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public ManySheetWriteHandler manySheetWriteHandler(ExcelConfigProperties configProperties) {
		return new ManySheetWriteHandler(configProperties);
	}

	/**
	 * 单sheet处理器
	 * @param configProperties Excle导出配置
	 * @return SingleSheetWriteHandler
	 */
	@Bean
	@ConditionalOnMissingBean
	public SingleSheetWriteHandler singleSheetWriteHandler(ExcelConfigProperties configProperties) {
		return new SingleSheetWriteHandler(configProperties);
	}


	@Override
	public void afterPropertiesSet() {
		RequestMappingHandlerAdapter handlerAdapter = applicationContext.getBean(RequestMappingHandlerAdapter.class);
		List<HandlerMethodReturnValueHandler> returnValueHandlers = handlerAdapter.getReturnValueHandlers();

		List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
		newHandlers.add(new ResponseExcelReturnValueHandler(sheetWriteHandlerList));
		assert returnValueHandlers != null;
		newHandlers.addAll(returnValueHandlers);
		handlerAdapter.setReturnValueHandlers(newHandlers);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
