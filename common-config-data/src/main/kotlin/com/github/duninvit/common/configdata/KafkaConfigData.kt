package com.github.duninvit.common.configdata

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "kafka-config")
data class KafkaConfigData(
    val bootstrapServers: String,
    val schemaRegistryUrlKey: String,
    val schemaRegistryUrl: String,
    val topicName: String,
    val topicNamesToCreate: List<String>,
    val numOfPartitions: Int,
    val replicationFactor: Short
)
