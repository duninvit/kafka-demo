package com.github.duninvit.kafkaproducer.config

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.KafkaProducerConfigData
import org.apache.avro.specific.SpecificRecordBase
import org.apache.kafka.clients.producer.ProducerConfig.ACKS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.BATCH_SIZE_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.COMPRESSION_TYPE_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.LINGER_MS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.RETRIES_CONFIG
import org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaProducerConfiguration<K : java.io.Serializable, V : SpecificRecordBase>(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducerConfigData: KafkaProducerConfigData
) {

    @Bean
    fun producerConfig(): Map<String, Any> =
        mapOf(
            BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers,
            kafkaConfigData.schemaRegistryUrlKey to kafkaConfigData.schemaRegistryUrl,
            KEY_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigData.keySerializerClass,
            VALUE_SERIALIZER_CLASS_CONFIG to kafkaProducerConfigData.valueSerializerClass,
            BATCH_SIZE_CONFIG to kafkaProducerConfigData.batchSize * kafkaProducerConfigData.batchSizeBoostFactor,
            LINGER_MS_CONFIG to kafkaProducerConfigData.lingerMs,
            COMPRESSION_TYPE_CONFIG to kafkaProducerConfigData.compressionType,
            ACKS_CONFIG to kafkaProducerConfigData.acks,
            REQUEST_TIMEOUT_MS_CONFIG to kafkaProducerConfigData.requestTimeoutMs,
            RETRIES_CONFIG to kafkaProducerConfigData.retryCount
        )


    @Bean
    fun producerFactory(): ProducerFactory<K, V> = DefaultKafkaProducerFactory(producerConfig())

    @Bean
    fun kafkaTemplate(): KafkaTemplate<K, V> = KafkaTemplate(producerFactory())
}
