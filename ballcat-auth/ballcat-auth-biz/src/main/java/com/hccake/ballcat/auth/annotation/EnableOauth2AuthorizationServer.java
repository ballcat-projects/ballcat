package com.hccake.ballcat.auth.annotation;

import com.hccake.ballcat.auth.configuration.AuthorizationAutoConfiguration;
import com.hccake.ballcat.auth.configuration.AuthorizationFilterConfiguration;
import com.hccake.ballcat.auth.configuration.CustomAuthorizationServerEndpointsConfiguration;
import com.hccake.ballcat.auth.configuration.CustomAuthorizationServerSecurityConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启 Oauth2 授权服务器
 *
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Deprecated
@Import({ AuthorizationFilterConfiguration.class, AuthorizationAutoConfiguration.class,
		CustomAuthorizationServerEndpointsConfiguration.class, CustomAuthorizationServerSecurityConfiguration.class })
public @interface EnableOauth2AuthorizationServer {

}
