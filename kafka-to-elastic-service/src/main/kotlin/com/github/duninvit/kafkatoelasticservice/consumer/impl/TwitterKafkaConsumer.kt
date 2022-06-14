package com.github.duninvit.kafkatoelasticservice.consumer.impl

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.kafka.admin.client.KafkaAdminClient
import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import com.github.duninvit.kafkatoelasticservice.consumer.KafkaConsumer
import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component

@Component
class TwitterKafkaConsumer(
    private val kafkaListenerEndpointRegistry: KafkaListenerEndpointRegistry,
    private val kafkaAdminClient: KafkaAdminClient,
    private val kafkaConfigData: KafkaConfigData
) : KafkaConsumer<Long, TwitterAvroModel> {

    private val logger = KotlinLogging.logger {}

    @KafkaListener(id = "twitterTopicListener", topics = ["\${kafka-config.topic-name}"])
    override fun receive(
        @Payload messages: List<TwitterAvroModel>,
        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) keys: List<Int>,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partitions: List<Int>,
        @Header(KafkaHeaders.OFFSET) offsets: List<Int>
    ) {
        logger.info {
            "${messages.size} number of message received with keys $keys, partitions $partitions and" +
                    " offsets $offsets, sending it to elastic: Thread id ${Thread.currentThread().id}"
        }
    }
}
