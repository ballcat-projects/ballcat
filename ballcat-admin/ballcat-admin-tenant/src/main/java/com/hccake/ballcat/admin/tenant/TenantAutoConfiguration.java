package com.hccake.ballcat.admin.tenant;

import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import com.hccake.ballcat.tenant.datasource.SysDataSourceDetailServiceImpl;
import com.hccake.ballcat.tenant.service.SysDatasourceService;
import com.hccake.ballcat.tenant.service.SysTenantDatasourceService;
import com.hccake.ballcat.tenant.service.SysTenantService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 租戶注冊
 *
 * @author huyuanzhi
 */
@ComponentScan("com.hccake.ballcat.tenant")
public class TenantAutoConfiguration {

	@Configuration(proxyBeanMethods = false)
	@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "enable", havingValue = "true")
	static class DataSourceDetailServiceConfiguration {

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "tenantType", havingValue = "DATASOURCE")
		public DataSourceDetailService dataSourceDetailService(SysTenantService sysTenantService,
				SysDatasourceService sysDatasourceService, SysTenantDatasourceService sysTenantDatasourceService) {
			return new SysDataSourceDetailServiceImpl(sysTenantService, sysDatasourceService,
					sysTenantDatasourceService);
		}

	}

}
