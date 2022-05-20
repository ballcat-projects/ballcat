package com.hccake.starter.dingtalk;

import com.hccake.extend.dingtalk.DingTalkSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2020/6/11 23:14
 */
@Slf4j
@RequiredArgsConstructor
@AutoConfiguration
@ConditionalOnProperty(name = "ballcat.dingtalk.url")
@EnableConfigurationProperties({ DingTalkProperties.class })
public class DingTalkAutoConfiguration {

	private final DingTalkProperties dingTalkProperties;

	@Bean
	@ConditionalOnMissingBean
	public DingTalkSender dingTalkSender() {
		return new DingTalkSender(dingTalkProperties.getUrl()).setSecret(dingTalkProperties.getSecret());
	}

}
