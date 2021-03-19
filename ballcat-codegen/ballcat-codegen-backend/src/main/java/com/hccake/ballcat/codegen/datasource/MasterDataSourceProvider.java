package com.hccake.ballcat.codegen.datasource;

import com.baomidou.dynamic.datasource.provider.AbstractDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.hccake.ballcat.codegen.constant.DataSourceConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 默认只提供主数据源，生成使用数据源，启动后再加载，避免影响项目启动
 *
 * @see DynamicJdbcDataSourceLoader
 * @author Hccake
 * @version 1.0
 * @date 2020/6/15 17:46
 */
@Slf4j
public class MasterDataSourceProvider extends AbstractDataSourceProvider {

	/**
	 * JDBC url 地址
	 */
	private final String url;

	/**
	 * JDBC 用户名
	 */
	private final String username;

	/**
	 * JDBC 密码
	 */
	private final String password;

	public MasterDataSourceProvider(DataSourceProperties dataSourceProperties) {
		this.url = dataSourceProperties.getUrl();
		this.username = dataSourceProperties.getUsername();
		this.password = dataSourceProperties.getPassword();
	}

	/**
	 * 加载所有数据源
	 * @return 所有数据源，key为数据源名称
	 */
	@Override
	public Map<String, DataSource> loadDataSources() {
		// 添加主数据源
		Map<String, DataSourceProperty> dataSourcePropertiesMap = new HashMap<>(8);
		DataSourceProperty masterDataSourceProperty = new DataSourceProperty();
		masterDataSourceProperty.setUsername(username);
		masterDataSourceProperty.setPassword(password);
		masterDataSourceProperty.setUrl(url);
		dataSourcePropertiesMap.put(DataSourceConstants.DEFAULT_DS_NAME, masterDataSourceProperty);

		return createDataSourceMap(dataSourcePropertiesMap);
	}

}
