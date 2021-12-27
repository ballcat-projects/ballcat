package com.hccake.ballcat.common.tenant.properties;

import com.hccake.ballcat.common.tenant.core.DatasourceType;
import com.hccake.ballcat.common.tenant.core.TenantType;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * 租户配置
 *
 * @author huyuanzhi
 */
@Data
@ConfigurationProperties(TenantProperties.PREFIX)
public class TenantProperties {

	public static final String PREFIX = "ballcat.db.tenant";

	private static final PathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private boolean enable = false;

	private TenantType tenantType = TenantType.NONE;

	private String tenantColumn = "tenant_code";

	private List<String> ignoreTables = new ArrayList<>();

	private String headerTenantKey = "tenant";

	private List<String> ignorePath = new ArrayList<>();

	private DatasourceType datasourceType = DatasourceType.HIKARI;

	private String secretKey = "ballcat";

	public boolean isIgnoreTenantUri(String path) {
		return ignorePath.stream().anyMatch(url -> path.startsWith(url) || ANT_PATH_MATCHER.match(url, path));
	}

}
