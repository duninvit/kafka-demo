package com.github.duninvit.kafkaproducer.service.impl

import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import com.github.duninvit.kafkaproducer.service.KafkaProducer
import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Service
import org.springframework.util.concurrent.ListenableFuture
import org.springframework.util.concurrent.ListenableFutureCallback
import javax.annotation.PreDestroy

@Service
class TwitterKafkaProducer(
    private val kafkaTemplate: KafkaTemplate<Long, TwitterAvroModel>
) : KafkaProducer<Long, TwitterAvroModel> {

    private val logger = KotlinLogging.logger {}

    override fun send(topicName: String, key: Long, value: TwitterAvroModel) {
        logger.info { "Sending message='$value' to topic='$topicName'" }
        kafkaTemplate.send(topicName, key, value)
            .addTwitterCallback(topicName, value)
    }

    @PreDestroy
    fun close() {
        logger.info { "Closing kafka producer" }
        kafkaTemplate.destroy()
    }

    private fun ListenableFuture<SendResult<Long, TwitterAvroModel>>.addTwitterCallback(
        topicName: String,
        value: TwitterAvroModel
    ) = addCallback(object : ListenableFutureCallback<SendResult<Long, TwitterAvroModel>> {
        override fun onSuccess(result: SendResult<Long, TwitterAvroModel>?) {
            result?.recordMetadata?.let {
                logger.debug {
                    "Received new metadata. Topic: ${it.topic()}; Partition: ${it.partition()}; " +
                            "Offset: ${it.offset()}; Timestamp: ${it.timestamp()}, at time: ${System.nanoTime()}"
                }
            }
        }

        override fun onFailure(ex: Throwable) {
            logger.error(ex) { "Error while sending message $value to topic $topicName" }
        }
    })
}
