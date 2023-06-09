package org.ballcat.security.web.configuration;

import org.ballcat.security.configuration.SecurityAuthorizationConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;

/**
 * @author lingting 2023-03-29 21:14
 */
@SuppressWarnings("java:S2094")
@AutoConfiguration
@ConditionalOnBean(SecurityAuthorizationConfiguration.class)
public class SecurityWebAuthorizationConfiguration {

}
