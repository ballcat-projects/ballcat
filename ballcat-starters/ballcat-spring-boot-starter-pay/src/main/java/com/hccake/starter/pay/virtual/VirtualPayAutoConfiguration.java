package com.hccake.starter.pay.virtual;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import live.lingting.virtual.currency.properties.InfuraProperties;
import live.lingting.virtual.currency.properties.OmniProperties;
import live.lingting.virtual.currency.properties.TronscanProperties;
import live.lingting.virtual.currency.service.impl.BtcOmniServiceImpl;
import live.lingting.virtual.currency.service.impl.InfuraServiceImpl;
import live.lingting.virtual.currency.service.impl.TronscanServiceImpl;

/**
 * @author lingting 2021/1/5 9:52
 */
@Slf4j
@RequiredArgsConstructor
@ConditionalOnClass(InfuraProperties.class)
@EnableConfigurationProperties({ BitcoinProperties.class, EthereumProperties.class,
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
	@ConditionalOnProperty(prefix = "ballcat.pay.ethereum", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public InfuraProperties infuraProperties(EthereumProperties properties) {
		EthereumProperties.Infura infura = properties.getInfura();
		return new InfuraProperties()
				// 节点
				.setEndpoints(infura.getEndpoints())
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
	@ConditionalOnBean(InfuraProperties.class)
	public InfuraServiceImpl infuraService(InfuraProperties properties) {
		return new InfuraServiceImpl(properties);
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
	@ConditionalOnProperty(prefix = "ballcat.pay.tronscan", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public TronscanProperties tronscanProperties(com.hccake.starter.pay.virtual.TronscanProperties properties) {
		return new TronscanProperties()
				// 节点
				.setEndpoints(properties.getEndpoints());
	}

	/**
	 * tronscan 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(TronscanProperties.class)
	public TronscanServiceImpl tronscanService(TronscanProperties properties) {
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
	@ConditionalOnProperty(prefix = "ballcat.pay.bitcoin", name = "enabled", havingValue = "true",
			matchIfMissing = true)
	public OmniProperties bitcoinProperties(BitcoinProperties properties) {
		return new OmniProperties()
				// 节点
				.setOmniEndpoints(properties.getOmni().getEndpoints())
				// 比特节点
				.setBitcoinEndpoints(properties.getEndpoints());
	}

	/**
	 * bitcoin 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnBean(OmniProperties.class)
	public BtcOmniServiceImpl bitcoinService(OmniProperties properties) {
		return new BtcOmniServiceImpl(properties);
	}

}
