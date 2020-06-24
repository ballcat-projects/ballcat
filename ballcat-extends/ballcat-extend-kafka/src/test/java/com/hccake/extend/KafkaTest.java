package com.hccake.extend;

import com.hccake.extend.kafka.KafkaConsumerBuilder;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

/**
 * @author lingting 2020/6/23 17:07
 */
public class KafkaTest {

	public static void main(String[] args) {
		// KafkaProducer<String, String> producer = new KafkaProducerBuilder()
		KafkaConsumer<Object, Object> consumer = new KafkaConsumerBuilder().keyDeserializer(StringDeserializer.class)
				.valueDeserializer(StringDeserializer.class).groupId("group-id").addTopic("first")
				// .keySerializer(StringSerializer.class)
				// .valueSerializer(StringSerializer.class)
				.addBootstrapServers("192.168.1.3:50211").addBootstrapServers("192.168.1.3:50212").build();

		// producer.send("first", "测试消息");
		// while (true) {
		// ConsumerRecords<Object, Object> records = consumer.poll(Duration.ofSeconds(5));
		// records.forEach(record -> {
		// System.out.println(record.key());
		// System.out.println(record.value());
		// });
		// }
	}

}
