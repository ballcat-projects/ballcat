package com.hccake.starter.sms.properties.extra;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 新酷卡猫池配置
 *
 * @author lingting 2020/5/6 16:59
 */
@Data
public class XinKuKa {

	/**
	 * 猫池短信发送模式
	 */
	private Mode mode = Mode.random;

	/**
	 * 指定手机号
	 */
	private String number;

	/**
	 * 指定端口
	 */
	private String port;

	/**
	 * 数据库配置
	 */
	private DataBase dataBase;

	@Getter
	@AllArgsConstructor
	public enum Mode {

		/**
		 * 随机选择可用通道发送
		 */
		random,
		/**
		 * 匹配指定手机号发送
		 */
		number,
		/**
		 * 匹配指定端口发送
		 */
		@Deprecated
		port,

		;

	}

	@Data
	public static class DataBase {

		/**
		 * 使用哪种数据库
		 */
		private Kind kind = Kind.mysql;

		@Getter
		@AllArgsConstructor
		public enum Kind {

			/**
			 * 使用mysql 数据库
			 */
			mysql,

			;

		}

	}

}
