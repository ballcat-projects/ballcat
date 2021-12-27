package com.hccake.ballcat.tenant.datasource;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.hccake.ballcat.common.tenant.core.TenantDataSource;
import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.tenant.model.entity.SysDatasource;
import com.hccake.ballcat.tenant.model.entity.SysTenant;
import com.hccake.ballcat.tenant.service.SysDatasourceService;
import com.hccake.ballcat.tenant.service.SysTenantDatasourceService;
import com.hccake.ballcat.tenant.service.SysTenantService;
import lombok.AllArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author huyuanzhi
 */
@AllArgsConstructor
public class SysDataSourceDetailServiceImpl implements DataSourceDetailService {

	private final SysTenantService sysTenantService;

	private final SysDatasourceService sysDatasourceService;

	private final SysTenantDatasourceService sysTenantDatasourceService;

	@Override
	public List<TenantDataSource> getTenantDataSource() {
		return sysDatasourceService.list().stream().map(d -> {
			TenantDataSource tenantDataSource = BeanUtil.toBean(d, TenantDataSource.class);
			tenantDataSource.setPoolName(d.getName());
			return tenantDataSource;
		}).collect(Collectors.toList());
	}

	@Override
	public Map<String, String> getTenantDataSourceMapping() {
		Map<Long, String> dataSourceMap = sysDatasourceService.list().stream()
				.collect(Collectors.toMap(SysDatasource::getId, SysDatasource::getName));
		Map<Long, String> tenantMap = sysTenantService.list().stream()
				.collect(Collectors.toMap(SysTenant::getTenantId, SysTenant::getTenantCode));
		Map<String, String> mapping = new HashMap<>(tenantMap.size());
		sysTenantDatasourceService.list().forEach(std -> {
			String tenantCode = tenantMap.get(std.getTenantId());
			String dataSource = dataSourceMap.get(std.getDatasourceId());
			if (CharSequenceUtil.isAllNotEmpty(tenantCode, dataSource)) {
				mapping.put(tenantCode, dataSource);
			}
		});
		return mapping;
	}

}
