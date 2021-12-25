package com.hccake.ballcat.autoconfigure.tenant.config;

import com.hccake.ballcat.autoconfigure.tenant.ColumnTenantInterceptor;
import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author huyuanzhi
 */
@Slf4j
public class TenantInterceptorConfig implements WebMvcConfigurer {

	@Autowired
	private TenantProperties tenantProperties;

	@Autowired(required = false)
	private DataSourceDetailService dataSourceDetailService;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (tenantProperties.isEnable()) {
			registry.addInterceptor(new ColumnTenantInterceptor(tenantProperties, dataSourceDetailService));
			log.debug("租户模式已开启，注册租户拦截器完成！");
		}
	}

}
