package org.ballcat.springsecurity.oauth2.server.resource.annotation;

import org.ballcat.springsecurity.oauth2.server.resource.ResourceServerAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启 Oauth2 资源服务器
 * @author hccake
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(ResourceServerAutoConfiguration.class)
public @interface EnableOauth2ResourceServer {

}
