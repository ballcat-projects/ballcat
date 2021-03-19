package com.hccake.ballcat.codegen.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @author Hccake 2021/3/19
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DynamicDataSourceHelper {

	/**
	 * 密码加密工具
	 */
	private final StringEncryptor stringEncryptor;

	/**
	 * 数据连接池创建者
	 */
	private final DataSourceCreator dataSourceCreator;

	/**
	 * 动态路由数据连接
	 */
	@Resource(type = DataSource.class)
	private DynamicRoutingDataSource dynamicRoutingDataSource;

	/**
	 * 加密明文密码
	 * @param pass 明文密码
	 * @return 密文密码
	 */
	public String encryptPass(String pass) {
		return stringEncryptor.encrypt(pass);
	}

	/**
	 * 解密密码
	 * @param password 密文密码
	 * @return 明文密码
	 */
	public String decryptPassword(String password) {
		return stringEncryptor.decrypt(password);
	}

	/**
	 * 校验数据源是配置否可用
	 * @param dataSourceProperty 数据源配置信息
	 * @return boolean
	 */
	public boolean isErrorDataSourceProperty(DataSourceProperty dataSourceProperty) {
		try (Connection ignored = DriverManager.getConnection(dataSourceProperty.getUrl(),
				dataSourceProperty.getUsername(), dataSourceProperty.getPassword())) {
			if (log.isDebugEnabled()) {
				log.debug("check connection success, dataSourceProperty: {}", dataSourceProperty);
			}
		}
		catch (Exception e) {
			log.error("get connection error, dataSourceProperty: {}", dataSourceProperty, e);
			return true;
		}
		return false;
	}

	/**
	 * 添加动态数据源
	 * @param dataSourceProperty 数据源配置
	 */
	public void addDynamicDataSource(DataSourceProperty dataSourceProperty) {
		DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
		dynamicRoutingDataSource.addDataSource(dataSourceProperty.getPoolName(), dataSource);
	}

	/**
	 * 删除数据源
	 * @param name 数据源名称
	 */
	public void removeDataSource(String name) {
		dynamicRoutingDataSource.removeDataSource(name);
	}

	/**
	 * 获得数据源配置实体
	 * @param dsName 数据源名称
	 * @param url 数据库连接
	 * @param username 数据库用户名
	 * @param password 数据库密码
	 * @return 数据源配置
	 */
	public DataSourceProperty prodDataSourceProperty(String dsName, String url, String username, String password) {
		DataSourceProperty dataSourceProperty = new DataSourceProperty();
		dataSourceProperty.setPoolName(dsName);
		dataSourceProperty.setUrl(url);
		dataSourceProperty.setUsername(username);
		dataSourceProperty.setPassword(password);
		return dataSourceProperty;
	}

}
