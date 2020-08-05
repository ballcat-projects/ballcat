package com.hccake.starter.kafka;

import com.hccake.extend.kafka.KafkaExtendProducer;
import com.hccake.extend.kafka.KafkaProducerBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2020/7/28 21:17
 */
@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties({ KafkaProperties.class })
public class KafkaAutoConfiguration {

	@Bean
	public KafkaExtendProducer<String, String> stringKafkaExtendProducer(KafkaProperties properties) {
		KafkaProducerBuilder builder = new KafkaProducerBuilder()
				.addAllBootstrapServers(properties.getBootstrapServers()).putAll(properties.getExtend());

		if (properties.getKeySerializer() != null || properties.getKeySerializerClassName() != null) {
			builder.keySerializer(properties.getKeySerializer() == null ? properties.getKeySerializerClassName()
					: properties.getKeySerializer().getName());
		}

		if (properties.getValueSerializer() != null || properties.getValueSerializerClassName() != null) {
			builder.valueSerializer(properties.getValueSerializer() == null ? properties.getValueSerializerClassName()
					: properties.getValueSerializer().getName());
		}
		return builder.build();
	}

}
