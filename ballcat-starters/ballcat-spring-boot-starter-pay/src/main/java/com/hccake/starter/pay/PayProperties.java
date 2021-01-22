package com.hccake.starter.pay;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import live.lingting.virtual.currency.endpoints.BitcoinEndpoints;
import live.lingting.virtual.currency.endpoints.Endpoints;
import live.lingting.virtual.currency.endpoints.InfuraEndpoints;
import live.lingting.virtual.currency.endpoints.OmniEndpoints;
import live.lingting.virtual.currency.endpoints.TronscanEndpoints;

/**
 * @author lingting 2021/1/5 9:58
 */
@Data
@ConfigurationProperties(prefix = "ballcat.pay")
public class PayProperties {

	/**
	 * 以太坊
	 */
	private Ethereum ethereum;

	/**
	 * 波场
	 */
	private Tronscan tronscan;

	/**
	 * 比特
	 */
	private Bitcoin bitcoin;

	@Data
	public static class Ethereum {

		/**
		 * 使用 infura 平台. 网址 https://infura.io/dashboard
		 */
		private Infura infura;

		@Data
		public static class Infura {

			/**
			 * 节点
			 */
			private InfuraEndpoints endpoints;

			/**
			 * 在 <a href="https://infura.io/dashboard"/> 注册后, 创建的app的 projectId
			 */
			private String projectId;

			/**
			 * projectSecret
			 */
			private String projectSecret;

		}

	}

	@Data
	public static class Tronscan {

		/**
		 * 节点
		 */
		private TronscanEndpoints endpoints;

	}

	@Data
	public static class Bitcoin {

		/**
		 * 使用 omni 平台
		 */
		private Omni omni;

		/**
		 * 比特节点
		 */
		private BitcoinEndpoints endpoints;

		@Data
		public static class Omni {

			/**
			 * 节点
			 */
			private OmniEndpoints endpoints;

		}

	}

}
