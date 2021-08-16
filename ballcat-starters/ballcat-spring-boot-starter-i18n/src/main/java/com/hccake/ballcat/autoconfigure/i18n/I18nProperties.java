package com.hccake.ballcat.autoconfigure.i18n;

import com.hccake.ballcat.common.i18n.I18nOptions;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author hccake
 */
@ConfigurationProperties(prefix = "ballcat.i18n")
public class I18nProperties extends I18nOptions {

}
