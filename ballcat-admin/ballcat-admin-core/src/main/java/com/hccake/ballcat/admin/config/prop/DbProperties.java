package com.hccake.ballcat.admin.config.prop;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 数据库配置属性
 *
 * @author huyuanzhi
 */
@Data
@ConfigurationProperties("ballcat.db")
public class DbProperties {

	private Long maxLimit = 500L;

	private boolean optimizeJoin = false;

}
