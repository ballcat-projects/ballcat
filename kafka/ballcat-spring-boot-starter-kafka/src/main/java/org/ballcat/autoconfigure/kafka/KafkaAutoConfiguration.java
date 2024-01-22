/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.autoconfigure.kafka;

import lombok.extern.slf4j.Slf4j;
import org.ballcat.kafka.KafkaConsumerBuilder;
import org.ballcat.kafka.KafkaExtendProducer;
import org.ballcat.kafka.KafkaProducerBuilder;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author lingting 2020/7/28 21:17
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties({ KafkaProperties.class })
public class KafkaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(KafkaExtendProducer.class)
	public KafkaExtendProducer<String, String> stringKafkaExtendProducer(KafkaProperties properties) {
		KafkaProducerBuilder builder = new KafkaProducerBuilder()
			.addAllBootstrapServers(properties.getBootstrapServers())
			.putAll(properties.getExtend());

		builder.keySerializer(properties.getKeySerializerClassName());
		builder.valueSerializer(properties.getValueSerializerClassName());
		return builder.build();
	}

	@Bean
	@Scope("prototype")
	@ConditionalOnMissingBean(KafkaConsumerBuilder.class)
	public KafkaConsumerBuilder consumerBuilder(KafkaProperties properties) {
		return new KafkaConsumerBuilder().addAllBootstrapServers(properties.getBootstrapServers())
			.keyDeserializer(properties.getKeyDeserializerClassName())
			.valueDeserializer(properties.getValueDeserializerClassName())
			.groupId(properties.getGroupId());
	}

}
