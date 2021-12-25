package com.hccake.ballcat.common.tenant.plugin;

import com.baomidou.mybatisplus.extension.plugins.handler.TenantLineHandler;
import com.hccake.ballcat.common.tenant.core.TenantContext;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import lombok.AllArgsConstructor;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.StringValue;

/**
 * column 模式租户处理
 *
 * @author huyuanzhi
 */
@AllArgsConstructor
public class ColumnTenantHandler implements TenantLineHandler {

	private final TenantProperties tenantProperties;

	@Override
	public Expression getTenantId() {
		return new StringValue(TenantContext.get());
	}

	@Override
	public String getTenantIdColumn() {
		return tenantProperties.getTenantColumn();
	}

	@Override
	public boolean ignoreTable(String tableName) {
		return tenantProperties.getIgnoreTables().stream().anyMatch(tableName::equalsIgnoreCase);
	}

}
