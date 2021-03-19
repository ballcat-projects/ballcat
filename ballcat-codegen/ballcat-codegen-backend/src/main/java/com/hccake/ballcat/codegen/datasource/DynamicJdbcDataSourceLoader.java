package com.hccake.ballcat.codegen.datasource;

import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.hccake.ballcat.codegen.model.entity.DataSourceConfig;
import com.hccake.ballcat.codegen.service.DataSourceConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hccake 2021/3/19
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicJdbcDataSourceLoader implements InitializingBean {

	private final DataSourceConfigService dataSourceConfigService;

	private final DynamicDataSourceHelper dynamicDataSourceHelper;

	@Override
	public void afterPropertiesSet() throws Exception {
		// 查找所有配置的生成项目使用数据源
		List<DataSourceConfig> list = dataSourceConfigService.list();

		// 遍历添加进动态数据源中
		for (DataSourceConfig dataSourceConfig : list) {
			String dsName = dataSourceConfig.getName();
			String username = dataSourceConfig.getUsername();
			String password = dynamicDataSourceHelper.decryptPassword(dataSourceConfig.getPassword());
			String url = dataSourceConfig.getUrl();

			DataSourceProperty property = dynamicDataSourceHelper.prodDataSourceProperty(dsName, url, username,
					password);

			// 如果数据源异常，则不加载
			if (!dynamicDataSourceHelper.isErrorDataSourceProperty(property)) {
				dynamicDataSourceHelper.addDynamicDataSource(property);
			}
		}
	}

}
