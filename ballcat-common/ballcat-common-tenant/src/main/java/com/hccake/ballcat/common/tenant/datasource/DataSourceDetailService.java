package com.hccake.ballcat.common.tenant.datasource;

import com.hccake.ballcat.common.tenant.core.TenantDataSource;

import java.util.List;
import java.util.Map;

/**
 * 获取动态数据源链接属性接口
 *
 * @author huyuanzhi
 */
public interface DataSourceDetailService {

	/**
	 * 获取系统配置的租户数据源链接
	 * @return 数据源链接
	 */
	List<TenantDataSource> getTenantDataSource();

	/**
	 * 获取租户与数据源之间的映射
	 * @return 租户数据源映射
	 */
	Map<String, String> getTenantDataSourceMapping();

}
