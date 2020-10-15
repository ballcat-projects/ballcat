package com.hccake.ballcat.common.conf.mybatis;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hccake
 * @date 2020/04/19 默认配置MybatisPlus分页插件，通过conditional注解达到覆盖效用
 */
@Configuration
public class MybatisPlusConfig {

	/**
	 * MybatisPlusInterceptor 插件，默认提供分页插件</br>
	 * 如需其他MP内置插件，则需自定义该Bean
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(MybatisPlusInterceptor.class)
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
		return interceptor;
	}

	/**
	 * 自动填充处理类
	 * @return FillMetaObjectHandle
	 */
	@Bean
	@ConditionalOnMissingBean(FillMetaObjectHandle.class)
	public FillMetaObjectHandle fillMetaObjectHandle() {
		return new FillMetaObjectHandle();
	}

}
