package com.hccake.ballcat.autoconfigure.tenant;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.hccake.ballcat.common.tenant.core.DatasourceType;
import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.common.tenant.core.TenantDataSource;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * 动态加载租户数据源
 *
 * @author huyuanzhi
 */
@Slf4j
@Configuration
@AllArgsConstructor
@ConditionalOnBean(DataSourceDetailService.class)
@ConditionalOnProperty(prefix = TenantProperties.PREFIX, name = {"tenantType"}, havingValue = "DATASOURCE")
public class TenantDatasourceLoader implements CommandLineRunner {

	private final DataSourceDetailService dataSourceDetailService;

	private final TenantProperties tenantProperties;

	private final DataSource dataSource;

	private final DefaultDataSourceCreator dataSourceCreator;

	@Override
	public void run(String... args) throws Exception {
		DatasourceType datasourceType = tenantProperties.getDatasourceType();
		log.info("系统初始化完成，开始加载租户数据源,数据源类型:{}", datasourceType.name());
		for (TenantDataSource tenantDataSource : dataSourceDetailService.getTenantDataSource()) {
			DataSourceProperty dataSourceProperty = new DataSourceProperty();
			BeanUtils.copyProperties(tenantDataSource, dataSourceProperty);
			DynamicRoutingDataSource routingDataSource = (DynamicRoutingDataSource) dataSource;
			DataSource ds = dataSourceCreator.createDataSource(dataSourceProperty);
			routingDataSource.addDataSource(tenantDataSource.getPoolName(), ds);
			log.info("数据源：{}初始化完成！", tenantDataSource.getPoolName());
		}
	}

}
