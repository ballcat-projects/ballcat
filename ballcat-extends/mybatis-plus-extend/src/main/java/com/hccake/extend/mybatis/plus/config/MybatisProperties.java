package com.hccake.extend.mybatis.plus.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author lingting  2020-05-28 14:54:30
 */
@Data
@ConfigurationProperties(prefix = "gzm.mybatis-plus")
public class MybatisProperties {
	/**
	 * 全局忽略更新的字段
	 */
	private Set<String> ignoreFields = new HashSet<>();

	/**
	 * 自定义方法
	 */
	private List<Class<? extends AbstractMethod>> methods = new ArrayList<>();
}
