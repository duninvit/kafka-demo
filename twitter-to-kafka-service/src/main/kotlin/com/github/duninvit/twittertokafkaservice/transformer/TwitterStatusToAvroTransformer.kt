package com.github.duninvit.twittertokafkaservice.transformer

import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import org.springframework.stereotype.Component
import twitter4j.Status

@Component
class TwitterStatusToAvroTransformer {

    fun getTwitterAvroModelFromStatus(status: Status): TwitterAvroModel =
        TwitterAvroModel
            .newBuilder()
            .setId(status.id)
            .setUserId(status.id)
            .setText(status.text)
            .setCreatedAt(status.createdAt.time)
            .build()
}
