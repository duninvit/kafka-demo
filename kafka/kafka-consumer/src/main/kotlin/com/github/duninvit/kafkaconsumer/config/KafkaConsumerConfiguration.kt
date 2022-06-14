package com.github.duninvit.kafkaconsumer.config

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.KafkaConsumerConfigData
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.MAX_POLL_RECORDS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory

@EnableKafka
@Configuration
class KafkaConsumerConfiguration<K : java.io.Serializable, V : SpecificRecordBase>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaConsumerConfigData: KafkaConsumerConfigData
) {

    @Bean
    fun consumerConfigs(): Map<String, Any> =
        with(kafkaConsumerConfigData) {
            mapOf(
                BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers,
                kafkaConfigData.schemaRegistryUrlKey to kafkaConfigData.schemaRegistryUrl,
                KEY_DESERIALIZER_CLASS_CONFIG to keyDeserializer,
                VALUE_DESERIALIZER_CLASS_CONFIG to valueDeserializer,
                GROUP_ID_CONFIG to consumerGroupId,
                AUTO_OFFSET_RESET_CONFIG to autoOffsetReset,
                specificAvroReaderKey to specificAvroReader,
                SESSION_TIMEOUT_MS_CONFIG to sessionTimeoutMs,
                HEARTBEAT_INTERVAL_MS_CONFIG to heartbeatIntervalMs,
                MAX_POLL_INTERVAL_MS_CONFIG to maxPollIntervalMs,
                MAX_PARTITION_FETCH_BYTES_CONFIG to maxPartitionFetchBytesDefault * maxPartitionFetchBytesBoostFactor,
                MAX_POLL_RECORDS_CONFIG to maxPollRecords,
            )
        }

    @Bean
    fun consumerFactory(): ConsumerFactory<K, V> = DefaultKafkaConsumerFactory(consumerConfigs())

    @Bean
    fun kafkaListenerContainerFactory() = ConcurrentKafkaListenerContainerFactory<K, V>().apply {
        consumerFactory = consumerFactory()
        isBatchListener = kafkaConsumerConfigData.batchListener
        setConcurrency(kafkaConsumerConfigData.concurrencyLevel)
        setAutoStartup(kafkaConsumerConfigData.autoStartup)
        containerProperties.pollTimeout = kafkaConsumerConfigData.pollTimeoutMs
    }
}
