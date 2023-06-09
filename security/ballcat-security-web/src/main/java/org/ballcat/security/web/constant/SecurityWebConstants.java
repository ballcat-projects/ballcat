package org.ballcat.security.web.constant;

import lombok.experimental.UtilityClass;

/**
 * @author lingting 2023-04-28 12:35
 */
@UtilityClass
public class SecurityWebConstants {

	public static final String URI_LOGOUT = "authorization/logout";

	public static final String URI_PASSWORD = "authorization/password";

	public static final String URI_REFRESH = "authorization/refresh";

	public static final String URI_RESOLVE = "authorization/resolve";

	public static final String TOKEN_HEADER = "Authorization";

	public static final String TOKEN_PARAMETER = "token";

	public static final String TOKEN_TYPE_BEARER = "Bearer";

	public static final String TOKEN_DELIMITER = " ";

}
