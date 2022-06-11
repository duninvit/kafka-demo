package com.github.duninvit.common.config

import com.github.duninvit.common.configdata.RetryConfigData
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.backoff.ExponentialBackOffPolicy
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

@Configuration
class RetryConfiguration(
    private val retryConfigData: RetryConfigData
) {

    @Bean
    fun retryTemplate() = RetryTemplate().apply {
        this.setBackOffPolicy(buildBackOffPolicy())
        this.setRetryPolicy(buildRetryPolicy())
    }

    private fun buildRetryPolicy() = SimpleRetryPolicy().apply {
        this.maxAttempts = retryConfigData.maxAttempts
    }

    private fun buildBackOffPolicy() = ExponentialBackOffPolicy().apply {
        this.initialInterval = retryConfigData.initialIntervalMs
        this.maxInterval = retryConfigData.maxIntervalMs
        this.multiplier = retryConfigData.multiplier
    }
}
