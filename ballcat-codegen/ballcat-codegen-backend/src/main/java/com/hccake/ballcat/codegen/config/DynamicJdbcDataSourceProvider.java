package com.hccake.ballcat.codegen.config;

import com.baomidou.dynamic.datasource.provider.AbstractJdbcDataSourceProvider;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.hccake.ballcat.codegen.constant.DataSourceConstants;
import org.jasypt.encryption.StringEncryptor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/15 17:46
 */
public class DynamicJdbcDataSourceProvider extends AbstractJdbcDataSourceProvider {

	public static final String QUERY_DS_SQL = "select * from gen_data_source_config";

	private final DataSourceProperty masterDataSourceProperty;

	private final StringEncryptor stringEncryptor;

	public DynamicJdbcDataSourceProvider(StringEncryptor stringEncryptor, String url, String username,
			String password) {
		super(url, username, password);
		this.stringEncryptor = stringEncryptor;
		this.masterDataSourceProperty = new DataSourceProperty();
		this.masterDataSourceProperty.setUsername(username);
		this.masterDataSourceProperty.setPassword(password);
		this.masterDataSourceProperty.setUrl(url);
	}

	@Override
	protected Map<String, DataSourceProperty> executeStmt(Statement statement) throws SQLException {
		Map<String, DataSourceProperty> map = new HashMap<>(8);
		// 添加主数据源
		map.put(DataSourceConstants.DEFAULT_DS_NAME, masterDataSourceProperty);
		// 动态数据源
		ResultSet rs = statement.executeQuery(QUERY_DS_SQL);
		while (rs.next()) {
			String name = rs.getString(DataSourceConstants.DS_NAME_KEY);
			String username = rs.getString(DataSourceConstants.DS_USERNAME_KEY);
			String password = stringEncryptor.decrypt(rs.getString(DataSourceConstants.DS_PASSWORD_KEY));
			String url = rs.getString(DataSourceConstants.DS_URL_KEY);
			DataSourceProperty property = new DataSourceProperty();
			property.setUsername(username);
			property.setPassword(password);
			property.setUrl(url);
			map.put(name, property);
		}

		return map;
	}

}
