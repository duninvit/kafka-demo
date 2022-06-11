package com.github.duninvit.twittertokafkaservice.runner.impl

import com.github.duninvit.common.configdata.TwitterToKafkaServiceConfigData
import com.github.duninvit.twittertokafkaservice.listener.TwitterKafkaStatusListener
import com.github.duninvit.twittertokafkaservice.runner.StreamRunner
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.FilterQuery
import twitter4j.TwitterStream
import twitter4j.TwitterStreamFactory
import javax.annotation.PreDestroy

@Component
@ConditionalOnProperty(name = ["twitter-to-kafka-service.runner-type"], havingValue = "STANDARD")
internal class TwitterKafkaStreamRunner(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) : StreamRunner {

    private val logger = KotlinLogging.logger {}
    private var twitterStream: TwitterStream? = null

    override fun start() {
        twitterStream = TwitterStreamFactory().instance
        twitterStream?.addListener(twitterKafkaStatusListener)
        addFilter()
    }

    @PreDestroy
    fun shutdown() {
        if (twitterStream != null) {
            logger.info { "Closing twitter stream!" }
            twitterStream?.shutdown()
        }
    }

    private fun addFilter() {
        val keywords = twitterToKafkaServiceConfigData.twitterKeywords.toTypedArray()
        val query = FilterQuery(*keywords)
        twitterStream?.filter(query)
        logger.info { "Started filtering $keywords" }
    }
}
