package com.hccake.starter.pay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import live.lingting.virtual.currency.properties.InfuraProperties;
import live.lingting.virtual.currency.properties.OmniProperties;
import live.lingting.virtual.currency.properties.TronscanProperties;
import live.lingting.virtual.currency.service.impl.InfuraServiceImpl;
import live.lingting.virtual.currency.service.impl.OmniServiceImpl;
import live.lingting.virtual.currency.service.impl.TronscanServiceImpl;

/**
 * @author lingting 2021/1/5 9:52
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({ PayProperties.class })
public class PayAutoConfiguration {

	private final PayProperties properties;

	/*
	 * Ethereum 配置
	 */

	/**
	 * infura 配置类
	 * @author lingting 2021-01-05 10:40
	 */
	@Bean
	@ConditionalOnMissingBean
	public InfuraProperties infuraProperties() {
		PayProperties.Ethereum.Infura infura = properties.getEthereum().getInfura();
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
	public TronscanProperties tronscanProperties() {
		return new TronscanProperties()
				// 节点
				.setEndpoints(properties.getTronscan().getEndpoints());
	}

	/**
	 * tronscan 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
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
	public OmniProperties bitcoinProperties() {
		return new OmniProperties()
				// 节点
				.setEndpoints(properties.getBitcoin().getOmni().getEndpoints());
	}

	/**
	 * bitcoin 平台实现类
	 * @author lingting 2021-01-05 10:35
	 */
	@Bean
	@ConditionalOnMissingBean
	public OmniServiceImpl bitcoinService(OmniProperties properties) {
		return new OmniServiceImpl(properties);
	}

}
