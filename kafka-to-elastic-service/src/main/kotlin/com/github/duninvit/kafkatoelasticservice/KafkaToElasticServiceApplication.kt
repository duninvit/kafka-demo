package com.github.duninvit.kafkatoelasticservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.github.duninvit"])
class KafkaToElasticServiceApplication

fun main(args: Array<String>) {
    runApplication<KafkaToElasticServiceApplication>(*args)
}
