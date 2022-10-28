package com.hccake.ballcat.autoconfigure.web.exception;

import com.hccake.ballcat.autoconfigure.web.exception.enums.ExceptionHandleTypeEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2020/6/12 0:15
 */
@Data
@Configuration
@ConfigurationProperties(prefix = ExceptionHandleProperties.PREFIX)
public class ExceptionHandleProperties {

	public static final String PREFIX = "ballcat.exception";

	/**
	 * 处理类型, 新版不需要指定异常处理类型了, 但是已经指定的旧配置依然按照旧配置的逻辑运行.
	 * @deprecated 下版本删除
	 */
	@Deprecated
	private ExceptionHandleTypeEnum type = null;

	/**
	 * 是否开启异常通知
	 */
	private Boolean enabled = false;

	/**
	 * 是否同时忽略 配置的忽略异常类的子类, 默认忽略子类
	 */
	private Boolean ignoreChild = true;

	/**
	 * 忽略指定异常
	 */
	private Set<Class<? extends Throwable>> ignoreExceptions = new HashSet<>();

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
	private Set<String> receiveEmails = new HashSet<>(0);

	/**
	 * 接收异常的钉钉配置
	 */
	private DingTalkProperties dingTalk;

	/**
	 * 异常通知 钉钉配置
	 */
	@Data
	public static class DingTalkProperties {

		/**
		 * 是否艾特所有人
		 */
		private Boolean atAll = false;

		/**
		 * 发送配置
		 */
		private List<Sender> senders;

		@Data
		public static class Sender {

			/**
			 * Web hook 地址
			 */
			private String url;

			/**
			 * 密钥
			 */
			private String secret;

		}

	}

}
