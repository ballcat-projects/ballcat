package org.ballcat.security.web.aspectj;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.ballcat.common.util.AspectUtils;
import org.ballcat.security.SecurityConstant;
import org.ballcat.security.annotation.Authorize;
import org.ballcat.security.authorize.SecurityAuthorize;
import org.ballcat.security.web.properties.SecurityWebProperties;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * @author lingting 2023-03-30 14:04
 */
@Aspect
@RequiredArgsConstructor
@Order(SecurityConstant.ORDER_RESOURCE_ASPECTJ)
public class SecurityWebResourceAspectj {

	private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	private final SecurityWebProperties properties;

	private final SecurityAuthorize securityAuthorize;

	@Pointcut("(@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller) ) "
			+ "&& execution(@(@org.springframework.web.bind.annotation.RequestMapping *) * *(..))")
	public void pointCut() {
		//
	}

	@Around("pointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		// 不是忽略鉴权的uri. 走鉴权流程
		if (!isIgnoreUri()) {
			Authorize authorize = AspectUtils.getAnnotation(point, Authorize.class);
			securityAuthorize.valid(authorize);
		}
		return point.proceed();
	}

	protected String uri() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

		if (!(attributes instanceof ServletRequestAttributes)) {
			return null;
		}

		ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) attributes;
		HttpServletRequest request = servletRequestAttributes.getRequest();
		return request.getRequestURI();
	}

	protected boolean isIgnoreUri() {
		Set<String> ignoreAntPatterns = properties.getIgnoreAntPatterns();
		if (CollectionUtils.isEmpty(ignoreAntPatterns)) {
			return false;
		}
		String uri = uri();

		if (StringUtils.hasText(uri)) {
			return true;
		}

		for (String antPattern : ignoreAntPatterns) {
			if (ANT_PATH_MATCHER.match(antPattern, uri)) {
				return true;
			}
		}

		return false;
	}

}
