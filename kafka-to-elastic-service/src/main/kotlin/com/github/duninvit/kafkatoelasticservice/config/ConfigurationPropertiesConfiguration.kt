package com.github.duninvit.kafkatoelasticservice.config

import com.github.duninvit.common.configdata.ElasticConfigData
import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.KafkaConsumerConfigData
import com.github.duninvit.common.configdata.RetryConfigData
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        KafkaConfigData::class,
        KafkaConsumerConfigData::class,
        RetryConfigData::class,
        ElasticConfigData::class,
    ]
)
class ConfigurationPropertiesConfiguration
