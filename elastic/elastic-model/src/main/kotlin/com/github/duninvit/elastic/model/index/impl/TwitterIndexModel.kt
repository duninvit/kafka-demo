package com.github.duninvit.elastic.model.index.impl

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.duninvit.elastic.model.index.IndexModel
import org.springframework.data.elasticsearch.annotations.DateFormat
import org.springframework.data.elasticsearch.annotations.Document
import org.springframework.data.elasticsearch.annotations.Field
import org.springframework.data.elasticsearch.annotations.FieldType.Date
import java.time.LocalDateTime

@Document(indexName = "#{elasticConfigData.indexName}")
class TwitterIndexModel(
    @JsonProperty private val id: String,
    @JsonProperty val userId: Long,
    @JsonProperty val text: String,
    @JsonProperty
    @Field(type = Date, format = [DateFormat.custom], pattern = ["uuuu-MM-dd'T'HH:mm:ssZZ"])
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "uuuu-MM-dd'T'HH:mm:ssZZ")
    val createdAt: LocalDateTime
) : IndexModel {

    override fun getId() = id
}
