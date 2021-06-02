package com.hccake.ballcat.admin.config;

import com.hccake.ballcat.common.conf.mybatis.FillMetaObjectHandle;
import com.hccake.ballcat.common.conf.mybatis.MybatisPlusConfig;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author hccake
 * @date 2020/12/16
 */
@AutoConfigureBefore(MybatisPlusConfig.class)
@Configuration(proxyBeanMethods = false)
public class AdminMybatisPlusConfig {

	/**
	 * 自动填充处理类
	 * @return FillMetaObjectHandle
	 */
	@Bean
	@ConditionalOnMissingBean
	public FillMetaObjectHandle fillMetaObjectHandle() {
		return new AdminFillMetaObjectHandle();
	}

}
