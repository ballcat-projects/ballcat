package com.hccake.ballcat.auth;

import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 根据规范 <a href="https://tools.ietf.org/html/rfc7662" target="_blank">OAuth 2.0
 * Introspection Endpoint</a>: 在 token 无效时返回应返回 200，而不是抛出异常，响应体内容如下：
 *
 * <pre>
 * {
 *    "active": false
 * }
 * </pre>
 *
 * 由于 spring-security-oauth2 对于 {@link CheckTokenEndpoint} 的注册没有提供覆盖方法，所以这里使用
 * {@link BeanPostProcessor#postProcessAfterInitialization } 来动态代理该类，
 *
 * @author hccake
 */
public class CheckEndpointPostProcessor implements BeanPostProcessor {

	@SneakyThrows
	@Override
	public Object postProcessAfterInitialization(final Object beanInstance, String beanName) throws BeansException {
		// 代理 CheckTokenEndpoint
		Class<?> beanClass = beanInstance.getClass();
		if (beanClass == CheckTokenEndpoint.class) {
			CheckEndpointPostProxy proxy = new CheckEndpointPostProxy();
			return proxy.create((CheckTokenEndpoint) beanInstance);
		}
		return beanInstance;
	}

	/**
	 * CheckTokenEndpoint 代理类，使用 CGLIB 实现，拦截 {@link CheckTokenEndpoint#checkToken(String)}
	 * 方法中抛出的 {@link InvalidTokenException} 异常，返回 OAuth2 规范的响应数据
	 */
	static class CheckEndpointPostProxy implements MethodInterceptor {

		private CheckTokenEndpoint target;

		public Object create(CheckTokenEndpoint target) throws NoSuchFieldException, IllegalAccessException {
			this.target = target;
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(CheckTokenEndpoint.class);
			enhancer.setCallback(this);
			Field resourceServerTokenServices = CheckTokenEndpoint.class
					.getDeclaredField("resourceServerTokenServices");
			resourceServerTokenServices.setAccessible(true);
			return enhancer.create(new Class[] { ResourceServerTokenServices.class },
					new Object[] { resourceServerTokenServices.get(this.target) });
		}

		@Override
		public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
			if ("checkToken".equals(method.getName())) {
				try {
					return methodProxy.invoke(this.target, objects);
				}
				catch (InvalidTokenException ex) {
					Map<String, Boolean> response = new HashMap<>();
					response.put("active", false);
					return response;
				}
			}
			else {
				return methodProxy.invoke(this.target, objects);
			}
		}

	}

}
