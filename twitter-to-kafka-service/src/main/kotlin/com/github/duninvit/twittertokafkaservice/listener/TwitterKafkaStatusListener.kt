package com.github.duninvit.twittertokafkaservice.listener

import mu.KotlinLogging
import org.springframework.stereotype.Component
import twitter4j.Status
import twitter4j.StatusAdapter

@Component
internal class TwitterKafkaStatusListener : StatusAdapter() {

    private val logger = KotlinLogging.logger {}

    override fun onStatus(status: Status?) {
        logger.info { "Twitter status with text: ${status?.text}" }
    }
}
