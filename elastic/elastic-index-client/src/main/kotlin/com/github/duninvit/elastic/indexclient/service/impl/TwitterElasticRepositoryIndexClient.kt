package com.github.duninvit.elastic.indexclient.service.impl

import com.github.duninvit.elastic.indexclient.repository.TwitterElasticsearchIndexRepository
import com.github.duninvit.elastic.indexclient.service.ElasticIndexClient
import com.github.duninvit.elastic.model.index.impl.TwitterIndexModel
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "elastic-config", name = ["is-repository"], havingValue = "true", matchIfMissing = true)
class TwitterElasticRepositoryIndexClient(
    private val twitterElasticsearchIndexRepository: TwitterElasticsearchIndexRepository
) : ElasticIndexClient<TwitterIndexModel> {

    private val logger = KotlinLogging.logger {}

    override fun save(documents: List<TwitterIndexModel>): List<String> =
        twitterElasticsearchIndexRepository.saveAll(documents)
            .map { it.getId() }
            .also {
                logger.info {
                    "Documents indexed successfully with type: ${TwitterIndexModel::class.simpleName} and ids: $it"
                }
            }
}
