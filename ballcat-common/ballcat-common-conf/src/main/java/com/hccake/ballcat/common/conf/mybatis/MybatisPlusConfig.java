package com.hccake.ballcat.common.conf.mybatis;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author
 * @date 2017/10/29
 */
@Configuration
@MapperScan(basePackages = {"com.hccake.ballcat.**.mapper", "com.your.packages.**.mapper"})
public class MybatisPlusConfig {

	/**
	 * 分页插件
	 * @return PaginationInterceptor
	 */
	@Bean
	public PaginationInterceptor paginationInterceptor() {
		return new PaginationInterceptor();
	}

}
