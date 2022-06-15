package com.github.duninvit.common.configdata

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "elastic-config")
data class ElasticConfigData(
    val indexName: String,
    val connectionUrl: String,
    val connectionTimeoutMs: Int,
    val socketTimeoutMs: Int,
    val isRepository: Boolean
)
