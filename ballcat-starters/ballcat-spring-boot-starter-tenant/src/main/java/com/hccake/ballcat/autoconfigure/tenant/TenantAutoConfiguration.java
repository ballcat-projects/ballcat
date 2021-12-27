package com.hccake.ballcat.autoconfigure.tenant;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.dynamic.datasource.event.DataSourceInitEvent;
import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.provider.DynamicDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.support.DdConstants;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.common.tenant.plugin.ColumnTenantHandler;
import com.hccake.ballcat.common.tenant.plugin.DataSourceDecoder;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 租户自动配置类
 *
 * @author huyuanzhi
 */
@AllArgsConstructor
@EnableConfigurationProperties({ TenantProperties.class })
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "enable", havingValue = "true")
public class TenantAutoConfiguration {

	private final TenantProperties tenantProperties;

	@Bean
	@ConditionalOnMissingBean(TenantLineInnerInterceptor.class)
	@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "tenantType", havingValue = "COLUMN")
	public InnerInterceptor columnTenantInterceptor() {
		return new TenantLineInnerInterceptor(new ColumnTenantHandler(tenantProperties));
	}

	@Bean
	@ConditionalOnBean(DataSourceDetailService.class)
	@ConditionalOnMissingBean(DataSourceInitEvent.class)
	public DataSourceInitEvent dataSourceInitEvent() {
		return new DataSourceDecoder(tenantProperties);
	}

	@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = "tenantType", havingValue = "DATASOURCE")
	public DynamicDataSourceProvider masterDataSource(DataSourceProperties dataSourceProperties) {
		return new AbstractDataSourceProvider() {
			@Override
			public Map<String, DataSource> loadDataSources() {
				// 添加主数据源
				Map<String, DataSourceProperty> dataSourcePropertiesMap = new HashMap<>(8);
				DataSourceProperty masterDataSourceProperty = new DataSourceProperty();
				BeanUtil.copyProperties(dataSourceProperties, masterDataSourceProperty);
				dataSourcePropertiesMap.put(DdConstants.MASTER, masterDataSourceProperty);
				return createDataSourceMap(dataSourcePropertiesMap);
			}
		};
	}

}
