package org.ballcat.springsecurity.oauth2.server.resource.annotation;

import org.ballcat.springsecurity.oauth2.server.resource.ResourceServerAutoConfiguration;
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
@ImportAutoConfiguration(ResourceServerAutoConfiguration.class)
public @interface EnableOauth2ResourceServer {

}
