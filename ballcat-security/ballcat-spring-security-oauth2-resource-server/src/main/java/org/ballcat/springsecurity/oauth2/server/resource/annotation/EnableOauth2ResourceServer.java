package org.ballcat.springsecurity.oauth2.server.resource.annotation;

import org.ballcat.springsecurity.oauth2.server.resource.OAuth2ResourceServerAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;

import java.lang.annotation.*;

/**
 * 开启 Oauth2 资源服务器
 *
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@ImportAutoConfiguration(OAuth2ResourceServerAutoConfiguration.class)
public @interface EnableOauth2ResourceServer {

}
