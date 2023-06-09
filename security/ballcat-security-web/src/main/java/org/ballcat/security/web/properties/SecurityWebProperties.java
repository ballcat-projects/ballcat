package org.ballcat.security.web.properties;

import lombok.Data;
import org.ballcat.security.properties.SecurityProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Set;

/**
 * @author lingting 2023-03-29 21:31
 */
@Data
@ConfigurationProperties(SecurityWebProperties.PREFIX)
public class SecurityWebProperties {

	public static final String PREFIX = SecurityProperties.PREFIX + ".web";

	private Set<String> ignoreAntPatterns;

}
