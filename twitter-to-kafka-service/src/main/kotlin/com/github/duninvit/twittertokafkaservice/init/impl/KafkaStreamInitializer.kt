package com.github.duninvit.twittertokafkaservice.init.impl

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.kafka.admin.client.KafkaAdminClient
import com.github.duninvit.twittertokafkaservice.init.StreamInitializer
import mu.KotlinLogging
import org.springframework.stereotype.Component

@Component
class KafkaStreamInitializer(
    private val kafkaConfigData: KafkaConfigData,
    private val kafkaAdminClient: KafkaAdminClient
) : StreamInitializer {

    private val logger = KotlinLogging.logger {}

    override fun init() {
        kafkaAdminClient.createTopics()
        kafkaAdminClient.checkSchemaRegistry()
        logger.info { "Topics with name ${kafkaConfigData.topicNamesToCreate} is ready for operations!" }
    }
}
