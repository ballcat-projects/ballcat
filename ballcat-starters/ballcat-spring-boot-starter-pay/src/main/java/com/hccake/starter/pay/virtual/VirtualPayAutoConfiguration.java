package com.hccake.starter.pay.virtual;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import live.lingting.virtual.currency.bitcoin.BitcoinServiceImpl;
import live.lingting.virtual.currency.core.Contract;
import live.lingting.virtual.currency.etherscan.EtherscanServiceImpl;
import live.lingting.virtual.currency.tronscan.TronscanServiceImpl;

/**
 * @author lingting 2021/1/5 9:52
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(Contract.class)
@EnableConfigurationProperties({ BitcoinProperties.class, EtherscanProperties.class,
		com.hccake.starter.pay.virtual.TronscanProperties.class })
public class VirtualPayAutoConfiguration {

	/*
	 * Ethereum 配置
	 */

	/**
	 * infura 配置类
	 * @author lingting 2021-01-05 10:40
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(EtherscanServiceImpl.class)
	@ConditionalOnProperty(prefix = "ballcat.pay.ethereum", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public live.lingting.virtual.currency.etherscan.properties.EtherscanProperties infuraProperties(
			EtherscanProperties properties) {
		EtherscanProperties.Infura infura = properties.getInfura();
		return new live.lingting.virtual.currency.etherscan.properties.EtherscanProperties()
				// 节点
				.setEndpoints(properties.getEndpoints())
				// project id
				.setProjectId(infura.getProjectId())
				// project secret
				.setProjectSecret(infura.getProjectSecret());
	}

	/**
	 * infura 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(EtherscanServiceImpl.class)
	@ConditionalOnBean(live.lingting.virtual.currency.etherscan.properties.EtherscanProperties.class)
	public EtherscanServiceImpl infuraService(
			live.lingting.virtual.currency.etherscan.properties.EtherscanProperties properties) {
		return new EtherscanServiceImpl(properties);
	}

	/*
	 * Tronscan 配置
	 */

	/**
	 * tronscan 配置类
	 * @author lingting 2021-01-05 10:40
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(TronscanServiceImpl.class)
	@ConditionalOnProperty(prefix = "ballcat.pay.tronscan", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public live.lingting.virtual.currency.tronscan.properties.TronscanProperties tronscanProperties(
			TronscanProperties properties) {
		return new live.lingting.virtual.currency.tronscan.properties.TronscanProperties()
				// 节点
				.setEndpoints(properties.getEndpoints());
	}

	/**
	 * tronscan 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(live.lingting.virtual.currency.tronscan.properties.TronscanProperties.class)
	public TronscanServiceImpl tronscanService(
			live.lingting.virtual.currency.tronscan.properties.TronscanProperties properties) {
		return new TronscanServiceImpl(properties);
	}

	/*
	 * bitcoin 配置
	 */

	/**
	 * bitcoin 配置类
	 * @author lingting 2021-01-05 10:40
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnClass(BitcoinServiceImpl.class)
	@ConditionalOnProperty(prefix = "ballcat.pay.bitcoin", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties bitcoinProperties(
			BitcoinProperties properties) {
		return new live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties()
				// 比特节点
				.setEndpoints(properties.getEndpoints());
	}

	/**
	 * bitcoin 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties.class)
	public BitcoinServiceImpl bitcoinService(
			live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties properties) {
		return new BitcoinServiceImpl(properties);
	}

}
