package com.github.duninvit.kafka.admin.config

import com.github.duninvit.common.configdata.KafkaConfigData
import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.admin.AdminClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.retry.annotation.EnableRetry

@EnableRetry
@Configuration
class KafkaAdminConfiguration(
    private val kafkaConfigData: KafkaConfigData
) {

    @Bean
    fun adminClient(): AdminClient = AdminClient.create(
        mapOf(
            CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG to kafkaConfigData.bootstrapServers
        )
    )
}
