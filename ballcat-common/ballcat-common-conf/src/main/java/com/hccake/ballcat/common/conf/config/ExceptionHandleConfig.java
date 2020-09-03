package com.hccake.ballcat.common.conf.config;

import com.hccake.ballcat.common.conf.exception.enums.ExceptionHandleTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2020/6/12 0:15
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "ballcat.exception")
public class ExceptionHandleConfig {

	/**
	 * 处理类型
	 */
	private ExceptionHandleTypeEnum type = ExceptionHandleTypeEnum.NONE;

	/**
	 * 通知间隔时间 单位秒 默认 5分钟
	 */
	private long time = TimeUnit.MINUTES.toSeconds(5);

	/**
	 * 消息阈值 即便间隔时间没有到达设定的时间， 但是异常发生的数量达到阈值 则立即发送消息
	 */
	private int max = 5;

	/**
	 * 堆栈转string 的长度
	 */
	private int length = 3000;

	/**
	 * 接收异常通知邮件的邮箱
	 */
	private Set<String> receiveEmails = new HashSet<>();

}
