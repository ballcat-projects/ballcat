package com.hccake.ballcat.admin.modules.sys.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lingting 2020/6/24 21:46
 */
@Data
@ConfigurationProperties(prefix = "ballcat.admin.rule")
public class AdminRuleConfig {

	/**
	 * 指定id的用户为超级管理员
	 */
	private Set<String> id = new HashSet<>();

	/**
	 * 指定email的用户为超级管理员
	 */
	private Set<String> username = new HashSet<>();

	/**
	 * 指定phone的用户为超级管理员
	 */
	private Set<String> email = new HashSet<>();

	/**
	 * 指定username为超级管理员
	 */
	private Set<String> phone = new HashSet<>();

	/**
	 * email 满足某个正则表达式的用户为超级管理员
	 */
	private Set<String> usernameReg = new HashSet<>();

	/**
	 * phone 满足某个正则表达式的用户为超级管理员
	 */
	private Set<String> emailReg = new HashSet<>();

	/**
	 * username 满足某个正则表达式的用户为超级管理员
	 */
	private Set<String> phoneReg = new HashSet<>();

}
