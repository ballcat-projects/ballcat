package com.hccake.ballcat.codegen.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/6/18 13:37
 * 前端页面请求404问题
 */
@Slf4j
@Configuration
public class SpaRedirectFilterConfiguration {
	private final static AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

	@Bean
	public FilterRegistrationBean<?> spaRedirectFiler() {
		FilterRegistrationBean<OncePerRequestFilter> registration = new FilterRegistrationBean<>();
		registration.setFilter(createRedirectFilter());
		registration.addUrlPatterns("/*");
		registration.setName("frontendRedirectFiler");
		registration.setOrder(1);
		return registration;
	}

	private OncePerRequestFilter createRedirectFilter() {
		return new OncePerRequestFilter() {

			// Forwards all routes except '/index.html', '/200.html', '/favicon.ico', '/sw.js' '/api/', '/api/**'
			private final String REGEX = "(?!/actuator|/_nuxt|/static|/index\\.html|/200\\.html|/favicon\\.ico|/sw\\.js).*$";
			private final Pattern pattern = Pattern.compile(REGEX);
			@Override
			protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
				String requestUri = req.getRequestURI();
				if (pattern.matcher(requestUri).matches() && !"/".equals(requestUri)) {
					RequestDispatcher rd;
					if(SpaRedirectFilterConfiguration.ANT_PATH_MATCHER.match("/api/**", requestUri)){
						log.info("URL {} access the backend, redirecting...", requestUri);
						rd = req.getRequestDispatcher(requestUri.substring(4));
					}else {
						// Delegate/Forward to `/` if `pattern` matches and it is not `/`
						// Required because of 'mode: history'usage in frontend routing, see README for further details
						log.info("URL {} entered directly into the Browser, redirecting...", requestUri);
						rd = req.getRequestDispatcher("/");
					}
					rd.forward(req, res);
				} else {
					chain.doFilter(req, res);
				}
			}
		};
	}
}