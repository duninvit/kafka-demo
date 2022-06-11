package com.github.duninvit.twittertokafkaservice.listener

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import com.github.duninvit.kafkaproducer.service.KafkaProducer
import com.github.duninvit.twittertokafkaservice.transformer.TwitterStatusToAvroTransformer
import mu.KotlinLogging
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
internal class TwitterKafkaStatusListener(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaProducer: KafkaProducer<Long, TwitterAvroModel>,
    private val twitterStatusToAvroTransformer: TwitterStatusToAvroTransformer
) : StatusAdapter() {

    private val logger = KotlinLogging.logger {}

    override fun onStatus(status: Status) {
        logger.info { "Twitter status text ${status.text} sending to kafka topic ${kafkaConfigData.topicName}" }
        twitterStatusToAvroTransformer.getTwitterAvroModelFromStatus(status).also {
            kafkaProducer.send(kafkaConfigData.topicName, it.userId, it)
        }
    }
}
