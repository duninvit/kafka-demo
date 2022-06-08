package com.github.duninvit.twittertokafkaservice.config

import com.github.duninvit.twittertokafkaservice.runner.RunnerType
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
internal data class TwitterToKafkaServiceConfigData(
    val twitterKeywords: List<String>,
    val runnerType: RunnerType,
    val twitterV2Data: TwitterV2Data,
    val mockData: MockData,
) {

    internal data class MockData(
        val sleepMs: Long,
        val minTweetLength: Int,
        val maxTweetLength: Int,
    )

    internal data class TwitterV2Data(
        val baseUrl: String?,
        val rulesBaseUrl: String?,
        val bearerToken: String?,
    )
}
