package com.github.duninvit.kafkatoelasticservice.consumer.impl

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.KafkaConsumerConfigData
import com.github.duninvit.elastic.indexclient.service.ElasticIndexClient
import com.github.duninvit.elastic.model.index.impl.TwitterIndexModel
import com.github.duninvit.kafka.admin.client.KafkaAdminClient
import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import com.github.duninvit.kafkatoelasticservice.consumer.KafkaConsumer
import com.github.duninvit.kafkatoelasticservice.transformer.AvroToElasticModelTransformer
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.event.EventListener
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
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaConsumerConfigData: KafkaConsumerConfigData,
    private val avroToElasticModelTransformer: AvroToElasticModelTransformer,
    private val elasticClient: ElasticIndexClient<TwitterIndexModel>
) : KafkaConsumer<Long, TwitterAvroModel> {

    private val logger = KotlinLogging.logger {}

    @EventListener
    fun onApplicationStarted(event: ApplicationStartedEvent) {
        kafkaAdminClient.checkTopicsCreated()
        logger.info { "Topics with name ${kafkaConfigData.topicNamesToCreate} is ready for operations!" }
        checkNotNull(kafkaListenerEndpointRegistry.getListenerContainer(kafkaConsumerConfigData.consumerGroupId)).start()
    }

    @KafkaListener(id = "\${kafka-consumer-config.consumer-group-id}", topics = ["\${kafka-config.topic-name}"])
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
        elasticClient.save(avroToElasticModelTransformer.getElasticModels(messages)).also {
            logger.info { "Documents saved to elasticsearch with ids: $it" }
        }
    }
}
