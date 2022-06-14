package com.github.duninvit.common.configdata

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka-consumer-config")
data class KafkaConsumerConfigData(
    val keyDeserializer: String,
    val valueDeserializer: String,
    val consumerGroupId: String,
    val autoOffsetReset: String,
    val specificAvroReaderKey: String,
    val specificAvroReader: String,
    val batchListener: Boolean,
    val autoStartup: Boolean,
    val concurrencyLevel: Int,
    val sessionTimeoutMs: Int,
    val heartbeatIntervalMs: Int,
    val maxPollIntervalMs: Int,
    val maxPollRecords: Int,
    val maxPartitionFetchBytesDefault: Int,
    val maxPartitionFetchBytesBoostFactor: Int,
    val pollTimeoutMs: Long,
)
