package com.github.duninvit.kafkatoelasticservice.transformer

import com.github.duninvit.elastic.model.index.impl.TwitterIndexModel
import com.github.duninvit.kafka.avro.model.TwitterAvroModel
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@Component
class AvroToElasticModelTransformer {

    fun getElasticModels(avroModels: List<TwitterAvroModel>): List<TwitterIndexModel> =
        avroModels.map {
            TwitterIndexModel(
                id = it.id.toString(),
                userId = it.id,
                text = it.text,
                createdAt = LocalDateTime.ofInstant(Instant.ofEpochMilli(it.createdAt), ZoneId.systemDefault())
            )
        }
}
