package com.github.duninvit.twittertokafkaservice.config

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.KafkaProducerConfigData
import com.github.duninvit.common.configdata.RetryConfigData
import com.github.duninvit.common.configdata.TwitterToKafkaServiceConfigData
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(
    value = [
        TwitterToKafkaServiceConfigData::class,
        KafkaConfigData::class,
        KafkaProducerConfigData::class,
        RetryConfigData::class,
    ]
)
class ConfigurationPropertiesConfiguration
