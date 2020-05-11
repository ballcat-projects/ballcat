package com.hccake.ballcat.common.conf.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hccake
 * @date 2020/04/19
 * 默认配置MybatisPlus分页插件，通过conditional注解达到覆盖效用
 */
@Configuration
public class MybatisPlusConfig {

	/**
	 * 分页插件
	 * @return PaginationInterceptor
	 */
	@Bean
	@ConditionalOnMissingBean(PaginationInterceptor.class)
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
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
