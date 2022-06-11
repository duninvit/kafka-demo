package com.github.duninvit.kafka.admin.client

import com.github.duninvit.common.configdata.KafkaConfigData
import com.github.duninvit.common.configdata.RetryConfigData
import com.github.duninvit.kafka.admin.exception.KafkaClientException
import mu.KotlinLogging
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.CreateTopicsResult
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.admin.TopicListing
import org.springframework.http.HttpMethod.GET
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE
import org.springframework.retry.support.RetryTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient

@Component
class KafkaAdminClient(
    private val kafkaConfigData: KafkaConfigData,
    private val retryConfigData: RetryConfigData,
    private val adminClient: AdminClient,
    private val retryTemplate: RetryTemplate,
    private val webClient: WebClient
) {

    private val logger = KotlinLogging.logger {}

    fun createTopics() {
        val createTopicsResult = try {
            retryTemplate.execute<CreateTopicsResult, KafkaClientException> {
                val topicNames = kafkaConfigData.topicNamesToCreate
                logger.info { "Creating ${topicNames.size} topic(s), attempt ${it.retryCount}" }
                val kafkaTopics = topicNames.map {
                    NewTopic(it.trim(), kafkaConfigData.numOfPartitions, kafkaConfigData.replicationFactor)
                }
                adminClient.createTopics(kafkaTopics)
            }
        } catch (t: Throwable) {
            throw KafkaClientException("Reached max number of retry for creating kafka topic(s)!", t)
        }
        checkTopicsCreated()
    }

    fun checkTopicsCreated() {
        var topics = getTopics()
        var retryCount = 1
        with(retryConfigData) {
            var sleepTime = sleepTimeMs
            for (topic in kafkaConfigData.topicNamesToCreate) {
                while (!isTopicCreated(topics, topic)) {
                    checkMaxRetry(retryCount++, maxAttempts)
                    sleep(sleepTimeMs)
                    sleepTime *= multiplier.toInt()
                    topics = getTopics()
                }
            }
        }
    }

    fun checkSchemaRegistry() {
        var retryCount = 1
        with(retryConfigData) {
            var sleepTime = sleepTimeMs
            while (!getSchemaRegistryStatus().is2xxSuccessful) {
                checkMaxRetry(retryCount++, maxAttempts)
                sleep(sleepTimeMs)
                sleepTime *= multiplier.toInt()
            }
        }
    }

    private fun getSchemaRegistryStatus() = try {
        webClient
            .method(GET)
            .uri(kafkaConfigData.schemaRegistryUrl)
            .exchange()
            .map(ClientResponse::statusCode)
            .block() ?: SERVICE_UNAVAILABLE
    } catch (ex: Exception) {
        HttpStatus.SERVICE_UNAVAILABLE
    }

    private fun sleep(sleepTimeMs: Long) = try {
        Thread.sleep(sleepTimeMs)
    } catch (ex: InterruptedException) {
        throw KafkaClientException("Error while sleeping for waiting new created topic(s)!")
    }

    private fun checkMaxRetry(retryCount: Int, maxAttempts: Int) {
        if (retryCount > maxAttempts) {
            throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!")
        }
    }

    private fun isTopicCreated(topics: Collection<TopicListing>, topicName: String?): Boolean =
        if (topicName == null) {
            false
        } else {
            topics.any { it.name() == topicName }
        }

    private fun getTopics() = try {
        retryTemplate.execute<Collection<TopicListing>, KafkaClientException> {
            logger.info { "Reading kafka topics ${kafkaConfigData.topicNamesToCreate}, attempt ${it.retryCount}" }

            adminClient.listTopics().listings().get()
                ?.onEach { logger.debug { "Topic with name ${it.name()}" } }
        }
    } catch (t: Throwable) {
        throw KafkaClientException("Reached max number of retry for reading kafka topic(s)!", t)
    }

}
