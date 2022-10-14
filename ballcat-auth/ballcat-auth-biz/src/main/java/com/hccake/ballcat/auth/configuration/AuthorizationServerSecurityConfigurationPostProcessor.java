package com.hccake.ballcat.auth.configuration;

import cn.hutool.core.util.ReflectUtil;
import com.hccake.ballcat.auth.filter.FilterWrapper;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerSecurityConfiguration;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.lang.reflect.Method;
import java.util.List;

/**
 * AuthorizationServerSecurityConfiguration 后置处理器，用来添加过滤器到 BasicAuthenticationFilter 之后
 *
 * @author hccake
 */
public class AuthorizationServerSecurityConfigurationPostProcessor implements BeanPostProcessor {

	private final List<FilterWrapper> filterWrappers;

	public AuthorizationServerSecurityConfigurationPostProcessor(List<FilterWrapper> filterWrappers) {
		this.filterWrappers = filterWrappers;
	}

	@Override
	public Object postProcessAfterInitialization(final Object beanInstance, String beanName) {
		// 代理 AuthorizationServerSecurityConfiguration
		Class<?> beanClass = beanInstance.getClass();
		if (AuthorizationServerSecurityConfiguration.class.isAssignableFrom(beanClass)) {
			Proxy proxy = new Proxy(filterWrappers);
			return proxy.create((AuthorizationServerSecurityConfiguration) beanInstance);
		}
		return beanInstance;
	}

	static class Proxy implements MethodInterceptor {

		private final List<FilterWrapper> filterWrappers;

		private AuthorizationServerSecurityConfiguration target;

		public Proxy(List<FilterWrapper> filterWrappers) {
			this.filterWrappers = filterWrappers;
		}

		public Object create(AuthorizationServerSecurityConfiguration target) {
			this.target = target;
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(AuthorizationServerSecurityConfiguration.class);
			enhancer.setCallback(this);
			return enhancer.create();
		}

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			if ("init".equals(method.getName()) && objects != null
					&& WebSecurity.class.isAssignableFrom(objects[0].getClass())) {
				Method getHttp = ReflectUtil.getMethod(WebSecurityConfigurerAdapter.class, "getHttp");
				ReflectUtil.setAccessible(getHttp);
				HttpSecurity httpSecurity = (HttpSecurity) getHttp.invoke(target);
				for (FilterWrapper filterWrapper : filterWrappers) {
					httpSecurity.addFilterAfter(filterWrapper.getFilter(), BasicAuthenticationFilter.class);
				}
			}
			return methodProxy.invoke(this.target, objects);
		}

	}

}
