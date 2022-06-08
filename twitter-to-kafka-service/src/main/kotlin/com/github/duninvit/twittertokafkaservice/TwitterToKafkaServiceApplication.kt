package com.github.duninvit.twittertokafkaservice

import com.github.duninvit.twittertokafkaservice.config.TwitterToKafkaServiceConfigData
import com.github.duninvit.twittertokafkaservice.runner.StreamRunner
import mu.KotlinLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
internal class TwitterToKafkaServiceApplication(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val streamRunner: StreamRunner
) : CommandLineRunner {

    private val logger = KotlinLogging.logger {}

    override fun run(vararg args: String?) {
        logger.info { twitterToKafkaServiceConfigData.twitterKeywords }
        streamRunner.start()
    }
}

fun main(args: Array<String>) {
    runApplication<TwitterToKafkaServiceApplication>(*args)
}
