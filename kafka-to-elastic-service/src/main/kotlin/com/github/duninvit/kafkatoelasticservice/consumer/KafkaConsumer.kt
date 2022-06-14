package com.github.duninvit.kafkatoelasticservice.consumer

import org.apache.avro.specific.SpecificRecordBase

interface KafkaConsumer<K : java.io.Serializable, V : SpecificRecordBase> {

    fun receive(messages: List<V>, keys: List<Int>, partitions: List<Int>, offsets: List<Int>)
}
