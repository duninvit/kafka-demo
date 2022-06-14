package com.github.duninvit.twittertokafkaservice

import com.github.duninvit.twittertokafkaservice.init.StreamInitializer
import com.github.duninvit.twittertokafkaservice.runner.StreamRunner
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.github.duninvit"])
internal class TwitterToKafkaServiceApplication(
    private val streamInitializer: StreamInitializer,
    private val streamRunner: StreamRunner,
) : CommandLineRunner {

    private val logger = KotlinLogging.logger {}

    override fun run(vararg args: String?) {
        streamInitializer.init()
        streamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterToKafkaServiceApplication>(*args)
}
