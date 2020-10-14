package com.hccake.starter.kafka;

import com.hccake.extend.kafka.KafkaConsumerBuilder;
import com.hccake.extend.kafka.KafkaExtendProducer;
import com.hccake.extend.kafka.KafkaProducerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

/**
 * @author lingting 2020/7/28 21:17
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({ KafkaProperties.class })
public class KafkaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(KafkaExtendProducer.class)
	public KafkaExtendProducer<String, String> stringKafkaExtendProducer(KafkaProperties properties) {
		KafkaProducerBuilder builder = new KafkaProducerBuilder()
				.addAllBootstrapServers(properties.getBootstrapServers()).putAll(properties.getExtend());

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
				.valueDeserializer(properties.getValueDeserializerClassName()).groupId(properties.getGroupId());
	}

}
