package com.github.duninvit.kafkaproducer.service

import org.apache.avro.specific.SpecificRecordBase

interface KafkaProducer<K : java.io.Serializable, V : SpecificRecordBase> {

    fun send(topicName: String, key: K, value: V)
}
