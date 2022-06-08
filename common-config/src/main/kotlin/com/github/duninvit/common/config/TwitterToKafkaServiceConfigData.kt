package com.github.duninvit.common.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "twitter-to-kafka-service")
data class TwitterToKafkaServiceConfigData(
    val twitterKeywords: List<String>,
    val runnerType: RunnerType,
    val twitterV2Data: TwitterV2Data,
    val mockData: MockData,
) {

    data class MockData(
        val sleepMs: Long,
        val minTweetLength: Int,
        val maxTweetLength: Int,
    )

    data class TwitterV2Data(
        val baseUrl: String?,
        val rulesBaseUrl: String?,
        val bearerToken: String?,
    )

    enum class RunnerType {
        STANDARD,
        MODERN,
        MOCK
    }
}
