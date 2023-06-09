package org.ballcat.security.annotation;

import org.ballcat.security.configuration.SecurityAuthorizationConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author lingting 2023-03-29 21:08
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Import(SecurityAuthorizationConfiguration.class)
public @interface EnableAuthorizationServer {

}
