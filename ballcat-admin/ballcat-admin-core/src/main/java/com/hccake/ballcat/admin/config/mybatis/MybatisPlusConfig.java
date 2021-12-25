package com.hccake.ballcat.admin.config.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.ISqlInjector;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.hccake.extend.mybatis.plus.injector.CustomSqlInjector;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.util.List;

/**
 * @author hccake
 * @date 2020/04/19 默认配置MybatisPlus分页插件，通过conditional注解达到覆盖效用
 */
@Configuration
@AllArgsConstructor
public class MybatisPlusConfig {

	private final List<InnerInterceptor> innerInterceptors;

	private final List<AbstractMethod> injectMethods;

	/**
	 * MybatisPlusInterceptor 插件，默认提供分页插件</br>
	 * 如需其他MP内置插件，则需自定义该Bean
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		AnnotationAwareOrderComparator.sort(innerInterceptors);
		innerInterceptors.forEach(interceptor::addInnerInterceptor);
		return interceptor;
	}

	/**
	 * 自动填充处理类
	 * @return FillMetaObjectHandle
	 */
	@Bean
	@ConditionalOnMissingBean(MetaObjectHandler.class)
	public MetaObjectHandler fillMetaObjectHandle() {
		return new FillMetaObjectHandle();
	}

	/**
	 * 自定义批量插入方法注入
	 * @return ISqlInjector
	 */
	@Bean
	@ConditionalOnMissingBean(ISqlInjector.class)
	public ISqlInjector customSqlInjector() {
		return new CustomSqlInjector(injectMethods);
	}

}
