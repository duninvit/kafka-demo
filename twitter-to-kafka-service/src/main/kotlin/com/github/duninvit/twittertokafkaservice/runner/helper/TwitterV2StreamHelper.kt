package com.github.duninvit.twittertokafkaservice.runner.helper

import com.github.duninvit.common.config.TwitterToKafkaServiceConfigData
import com.github.duninvit.twittertokafkaservice.listener.TwitterKafkaStatusListener
import mu.KotlinLogging
import org.apache.http.client.config.CookieSpecs.STANDARD
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.utils.URIBuilder
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.util.EntityUtils
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import twitter4j.JSONArray
import twitter4j.JSONObject
import twitter4j.TwitterException
import twitter4j.TwitterObjectFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

@Component
@ConditionalOnProperty(name = ["twitter-to-kafka-service.runner-type"], havingValue = "MODERN")
internal class TwitterV2StreamHelper(
    private val twitterToKafkaServiceConfigData: TwitterToKafkaServiceConfigData,
    private val twitterKafkaStatusListener: TwitterKafkaStatusListener
) {

    private val logger = KotlinLogging.logger {}

    /**
     * This method calls the filtered stream endpoint and streams Tweets from it
     */
    //  throws IOException, URISyntaxException, JSONException
    fun connectStream(bearerToken: String) {
        val entity = getHttpClient().execute(
            HttpGet(URIBuilder(twitterToKafkaServiceConfigData.twitterV2Data.baseUrl).build()).also {
                it.setHeader("Authorization", "Bearer %s".format(bearerToken))
            }
        ).entity
        if (entity != null) {
            BufferedReader(InputStreamReader(entity.content)).use { reader ->
                var line = reader.readLine()
                while (line != null) {
                    line = reader.readLine()
                    if (!line.isNullOrEmpty()) {
                        val tweet = getFormattedTweet(line)
                        val status = try {
                            TwitterObjectFactory.createStatus(tweet)
                        } catch (ex: TwitterException) {
                            logger.error(ex) { "Could not create status for text: $tweet" }
                            throw RuntimeException(ex)
                        }
                        if (status != null) {
                            twitterKafkaStatusListener.onStatus(status)
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper method to setup rules before streaming data
     */
    // throws IOException, URISyntaxException
    fun setupRules(bearerToken: String, rules: Map<String, String>) {
        val existingRules = getRules(bearerToken)
        if (existingRules.isNotEmpty()) {
            deleteRules(bearerToken, existingRules)
        }
        createRules(bearerToken, rules)
        logger.info { "Created rules for twitter stream  ${rules.keys}" }
    }

    /**
     * Helper method to create rules for filtering
     */
    //throws URISyntaxException, IOException
    private fun createRules(bearerToken: String, rules: Map<String, String>) {
        val entity = getHttpClient().execute(
            HttpPost(URIBuilder(twitterToKafkaServiceConfigData.twitterV2Data.rulesBaseUrl).build()).also {
                it.setHeader("Authorization", "Bearer %s".format(bearerToken))
                it.setHeader("content-type", "application/json")
                it.entity = StringEntity(getFormattedString("{\"add\": [%s]}", rules))
            }
        ).entity
        if (entity != null) {
            logger.info { EntityUtils.toString(entity, "UTF-8") }
        }
    }

    /**
     * Helper method to get existing rules
     */
    //  throws URISyntaxException, IOException
    private fun getRules(bearerToken: String): List<String> {
        val rules = mutableListOf<String>()
        val entity = getHttpClient().execute(
            HttpGet(URIBuilder(twitterToKafkaServiceConfigData.twitterV2Data.rulesBaseUrl).build()).also {
                it.setHeader("Authorization", "Bearer %s".format(bearerToken))
                it.setHeader("content-type", "application/json")
            }
        ).entity
        if (entity != null) {
            val json = JSONObject(EntityUtils.toString(entity, "UTF-8"))
            if (json.length() > 1) {
                val array = json.get("data") as JSONArray
                for (i in 0 until array.length()) {
                    val jsonObject = array.get(i) as JSONObject
                    rules.add(jsonObject.getString("id"))
                }
            }
        }
        return rules
    }

    /**
     * Helper method to delete rules
     */
    // throws URISyntaxException, IOException
    private fun deleteRules(bearerToken: String, existingRules: List<String>) {
        val entity = getHttpClient().execute(
            HttpPost(URIBuilder(twitterToKafkaServiceConfigData.twitterV2Data.rulesBaseUrl).build()).also {
                it.setHeader("Authorization", "Bearer %s".format(bearerToken))
                it.setHeader("content-type", "application/json")
                it.entity = StringEntity(getFormattedString("{ \"delete\": { \"ids\": [%s]}}", existingRules))
            }
        ).entity
        if (entity != null) {
            logger.info { EntityUtils.toString(entity, "UTF-8") }
        }
    }

    private fun getHttpClient(): CloseableHttpClient =
        HttpClients.custom()
            .setDefaultRequestConfig(
                RequestConfig.custom()
                    .setCookieSpec(STANDARD)
                    .build()
            ).build()


    private fun getFormattedString(string: String, ids: List<String>): String =
        if (ids.size == 1) {
            String.format(string, "\"" + ids[0] + "\"")
        } else {
            buildString {
                for (id in ids) {
                    append("\"$id\",")
                }
            }.let { String.format(string, it.substring(0, it.length - 1)) }
        }


    private fun getFormattedString(string: String, rules: Map<String, String>): String =
        if (rules.size == 1) {
            val key = rules.keys.iterator().next()
            String.format(string, "{\"value\": \"" + key + "\", \"tag\": \"" + rules[key] + "\"}")
        } else {
            buildString {
                for (entry in rules.entries) {
                    append("{\"value\": \"${entry.key}\", \"tag\": \"${entry.value}\"},")
                }
            }.let { String.format(string, it.substring(0, it.length - 1)) }
        }

    private fun getFormattedTweet(data: String): String =
        (JSONObject(data).get("data") as JSONObject).let { jsonData ->
            arrayOf(
                ZonedDateTime.parse(jsonData.get("created_at").toString()).withZoneSameInstant(ZoneId.of("UTC"))
                    .format(DateTimeFormatter.ofPattern(TWITTER_STATUS_DATE_FORMAT, Locale.ENGLISH)),
                jsonData.get("id").toString(),
                jsonData.get("text").toString().replace("\"", "\\\\\""),
                jsonData.get("author_id").toString(),
            ).let { formatTweetAsJsonWithParams(it) }
        }


    private fun formatTweetAsJsonWithParams(params: Array<String>): String {
        var tweet = tweetAsRawJson
        for (i in params.indices) {
            tweet = tweet.replace("{$i}", params[i])
        }
        return tweet
    }

    companion object {
        private const val TWITTER_STATUS_DATE_FORMAT = "EEE MMM dd HH:mm:ss zzz yyyy"
        private const val tweetAsRawJson = "{" +
                "\"created_at\":\"{0}\"," +
                "\"id\":\"{1}\"," +
                "\"text\":\"{2}\"," +
                "\"user\":{\"id\":\"{3}\"}" +
                "}"
    }
}
