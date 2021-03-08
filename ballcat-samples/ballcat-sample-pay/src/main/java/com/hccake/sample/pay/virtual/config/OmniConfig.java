package com.hccake.sample.pay.virtual.config;

import com.hccake.starter.pay.virtual.BitcoinProperties;
import java.util.function.Supplier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lingting 2021/1/5 16:24
 */
@Configuration
public class OmniConfig {

	@Bean
	public live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties bitcoinProperties(
			BitcoinProperties properties) {
		// omni 使用的接口限制请求频率(5-10s一次), 需要根据项目实现, 这里直接返回true
		Supplier<Boolean> lock = () -> true;
		Supplier<Boolean> unlock = () -> true;

		return new live.lingting.virtual.currency.bitcoin.properties.BitcoinProperties()
				// 比特节点
				.setEndpoints(properties.getEndpoints())
				// 请求锁
				.setLock(lock).setUnlock(unlock);
	}

}
