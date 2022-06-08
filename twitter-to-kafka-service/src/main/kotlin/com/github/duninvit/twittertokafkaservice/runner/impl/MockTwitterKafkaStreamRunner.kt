package com.github.duninvit.twittertokafkaservice.runner.impl

import com.github.duninvit.common.config.TwitterToKafkaServiceConfigData
import com.github.duninvit.twittertokafkaservice.listener.TwitterKafkaStatusListener
import com.github.duninvit.twittertokafkaservice.runner.StreamRunner
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.TwitterObjectFactory
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadLocalRandom

@Component
@ConditionalOnProperty(name = ["twitter-to-kafka-service.runner-type"], havingValue = "MOCK")
internal class MockTwitterKafkaStreamRunner(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) : StreamRunner {

    private val logger = KotlinLogging.logger {}

    override fun start() {
        val keywords = twitterToKafkaServiceConfigData.twitterKeywords
        with(twitterToKafkaServiceConfigData.mockData) {
            logger.info { "Starting mock filtering twitter stream for keywords: $keywords" }
            simulateTweetStream(keywords, minTweetLength, maxTweetLength, sleepMs)
        }
    }

    private fun simulateTweetStream(
        keywords: List<String>, minTweetLength: Int, maxTweetLength: Int, sleepTimeMs: Long
    ) = Executors.newSingleThreadExecutor().submit {
        try {
            while (true) {
                val formattedTweetAsRawJson = getFormattedTweet(keywords, minTweetLength, maxTweetLength)
                val status = TwitterObjectFactory.createStatus(formattedTweetAsRawJson)
                twitterKafkaStatusListener.onStatus(status)
                sleep(sleepTimeMs)
            }
        } catch (ex: RuntimeException) {
            logger.error(ex) { "Error creating tweet status" }
        }
    }


    private fun sleep(sleepTimeMs: Long) = try {
        Thread.sleep(sleepTimeMs)
    } catch (ex: InterruptedException) {
        throw RuntimeException("Error while sleeping for waiting new status to create!")
    }

    private fun getFormattedTweet(keywords: List<String>, minTweetLength: Int, maxTweetLength: Int): String = arrayOf(
        ZonedDateTime.now().format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
        ThreadLocalRandom.current().nextLong(Long.MAX_VALUE).toString(),
        getRandomTweetContent(keywords, minTweetLength, maxTweetLength),
        ThreadLocalRandom.current().nextLong(Long.MAX_VALUE).toString(),
    ).let {
        var tweet = tweetAsRawJson
        for (i in keywords.indices) {
            tweet = tweet.replace("{$i}", it[i])
        }
        tweet
    }

    private fun getRandomTweetContent(keywords: List<String>, minTweetLength: Int, maxTweetLength: Int): String =
        buildString {
            val tweetLength = RANDOM.nextInt(maxTweetLength - minTweetLength + 1) + maxTweetLength
            for (i in 0 until tweetLength) {
                this.append(WORDS[RANDOM.nextInt(WORDS.size)]).append(" ")
                if (i == this.length / 2) {
                    this.append(keywords[RANDOM.nextInt(keywords.size)]).append(" ")
                }
            }
        }.trim()

    companion object {
        private val RANDOM = Random()
        private val WORDS = arrayOf(
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor",
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero",
        )
        private const val TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
        private const val tweetAsRawJson =
            "{\"created_at\":\"{0}\",\"id\":\"{1}\",\"text\":\"{2}\",\"user\":{\"id\":\"{3}\"}}"
    }
}
