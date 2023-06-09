package org.ballcat.security.web.configuration;

import org.ballcat.security.web.properties.SecurityWebProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * @author lingting 2023-03-29 21:13
 */
@AutoConfiguration
@EnableConfigurationProperties(SecurityWebProperties.class)
public class SecurityWebConfiguration {

}
