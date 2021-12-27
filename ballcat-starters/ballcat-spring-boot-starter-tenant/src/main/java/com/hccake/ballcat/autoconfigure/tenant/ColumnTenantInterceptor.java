package com.hccake.ballcat.autoconfigure.tenant;

import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import com.hccake.ballcat.common.model.result.R;
import com.hccake.ballcat.common.model.result.SystemResultCode;
import com.hccake.ballcat.common.tenant.core.TenantContext;
import com.hccake.ballcat.common.tenant.core.TenantException;
import com.hccake.ballcat.common.tenant.core.TenantType;
import com.hccake.ballcat.common.tenant.datasource.DataSourceDetailService;
import com.hccake.ballcat.common.tenant.properties.TenantProperties;
import com.hccake.ballcat.common.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 租户模式下解析租户代码，并存入上下文
 *
 * @author huyuanzhi
 */
@Slf4j
@AllArgsConstructor
public class ColumnTenantInterceptor implements HandlerInterceptor {

	private final TenantProperties tenantProperties;

	private final DataSourceDetailService dataSourceDetailService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws IOException {
		try {
			parseTenant(request);
			return true;
		}
		catch (TenantException e) {
			response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON.toString());
			response.setHeader(HttpHeaders.ACCEPT_CHARSET, StandardCharsets.UTF_8.toString());
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			R<String> result = R.failed(SystemResultCode.UNAUTHORIZED, e.getMessage());
			response.getWriter().write(JsonUtils.toJson(result));
		}
		return false;
	}

	private void parseTenant(HttpServletRequest request) {
		if (isIgnoreTenant(request.getRequestURI())) {
			return;
		}
		String tenantCode = request.getHeader(tenantProperties.getHeaderTenantKey());
		if (!StringUtils.hasText(tenantCode)) {
			log.error("获取租户代码为空，uri:{}", request.getRequestURI());
			throw new TenantException("获取租户代码为空！");
		}
		TenantContext.set(tenantCode);
		// 如果是数据源模式，则需要切换数据源
		// TODO 暫時只有兩種模式，後期模式多了再抽象
		if (tenantProperties.getTenantType() == TenantType.DATASOURCE) {
			doDetermineDatasource(tenantCode);
		}
	}

	private void doDetermineDatasource(String tenantCode) {
		Map<String, String> mapping = dataSourceDetailService.getTenantDataSourceMapping();
		String dataSourceKey = mapping.get(tenantCode);
		if (!StringUtils.hasLength(dataSourceKey)) {
			log.error("获取数据源失败，租户代码:{},数据源映射:{}", tenantCode, mapping);
			throw new TenantException("获取租户数据源失败！");
		}
		DynamicDataSourceContextHolder.push(dataSourceKey);
	}

	private boolean isIgnoreTenant(String path) {
		return tenantProperties.isIgnoreTenantUri(path);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		TenantContext.clear();
	}

}
