package com.hccake.ballcat.common.security.annotation;

import com.hccake.ballcat.common.security.oauth2.server.resource.ResourceServerAutoConfiguration;
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
