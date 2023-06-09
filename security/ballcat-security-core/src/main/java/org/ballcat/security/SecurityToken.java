package org.ballcat.security;

import lombok.Data;

/**
 * @author lingting 2023-04-28 12:38
 */
@Data
public class SecurityToken {

	private String type;

	private String token;

	private String raw;

}
