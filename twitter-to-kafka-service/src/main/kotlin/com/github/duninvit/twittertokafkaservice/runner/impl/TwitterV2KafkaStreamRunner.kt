package com.github.duninvit.twittertokafkaservice.runner.impl

import com.github.duninvit.twittertokafkaservice.config.TwitterToKafkaServiceConfigData
import com.github.duninvit.twittertokafkaservice.runner.StreamRunner
import com.github.duninvit.twittertokafkaservice.runner.helper.TwitterV2StreamHelper
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component


@Component
@ConditionalOnProperty(name = ["twitter-to-kafka-service.runner-type"], havingValue = "MODERN")
internal class TwitterV2KafkaStreamRunner(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val twitterV2StreamHelper: TwitterV2StreamHelper
) : StreamRunner {

    private val logger = KotlinLogging.logger { }

    override fun start() =
        twitterToKafkaServiceConfigData.twitterV2Data.bearerToken?.let {
            try {
                twitterV2StreamHelper.setupRules(it, getRules())
                twitterV2StreamHelper.connectStream(it)
            } catch (ex: Exception) {
                logAndThrow("Error streaming tweets!")
            }
        } ?: logAndThrow(ERROR_MSG)

    private fun logAndThrow(msg: String): Nothing {
        logger.error(msg)
        throw RuntimeException(msg)
    }

    private fun getRules(): Map<String, String> {
        val keywords = twitterToKafkaServiceConfigData.twitterKeywords
        val rules = mutableMapOf<String, String>()
        for (keyword in keywords) {
            rules[keyword] = "Keyword: $keyword"
        }
        logger.info("Created filter for twitter stream for keywords: $keywords")
        return rules
    }

    companion object {
        private const val ERROR_MSG = "There was a problem getting your bearer token. " +
                "Please make sure you set the TWITTER_BEARER_TOKEN environment variable"
    }
}
