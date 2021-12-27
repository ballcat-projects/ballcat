package com.hccake.ballcat.common.tenant.plugin;

import com.baomidou.dynamic.datasource.event.DataSourceInitEvent;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.CryptoUtils;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;

/**
 * 动态数据源解密
 *
 * @author huyuanzhi
 */
@Slf4j
@AllArgsConstructor
public class DataSourceDecoder implements DataSourceInitEvent {

	private final TenantProperties tenantProperties;

	@Override
	public void beforeCreate(DataSourceProperty dataSourceProperty) {
		String secretKey = tenantProperties.getSecretKey();
		if (StringUtils.hasText(secretKey)) {
			dataSourceProperty.setUrl(this.decrypt(secretKey, dataSourceProperty.getUrl()));
			dataSourceProperty.setUsername(this.decrypt(secretKey, dataSourceProperty.getUsername()));
			dataSourceProperty.setPassword(this.decrypt(secretKey, dataSourceProperty.getPassword()));
		}
	}

	private String decrypt(String secretKey, String cipherText) {
		if (StringUtils.hasText(cipherText)) {
			try {
				return CryptoUtils.decrypt(secretKey, cipherText);
			}
			catch (Exception var5) {
				log.error("DynamicDataSourceProperties.decrypt error ", var5);
			}
		}
		return cipherText;
	}

	@Override
	public void afterCreate(DataSource dataSource) {
	}

}
