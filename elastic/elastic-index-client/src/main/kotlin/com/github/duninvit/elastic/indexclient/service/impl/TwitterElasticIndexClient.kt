package com.github.duninvit.elastic.indexclient.service.impl

import com.github.duninvit.common.configdata.ElasticConfigData
import com.github.duninvit.elastic.indexclient.helper.ElasticIndexHelper
import com.github.duninvit.elastic.indexclient.service.ElasticIndexClient
import com.github.duninvit.elastic.model.index.impl.TwitterIndexModel
import mu.KotlinLogging
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.stereotype.Service

@Service
@ConditionalOnProperty(prefix = "elastic-config", name = ["is-repository"], havingValue = "false")
class TwitterElasticIndexClient(
    private val elasticConfigData: ElasticConfigData,
    private val elasticsearchOperations: ElasticsearchOperations,
    private val elasticIndexHelper: ElasticIndexHelper<TwitterIndexModel>
) : ElasticIndexClient<TwitterIndexModel> {

    private val logger = KotlinLogging.logger {}

    override fun save(documents: List<TwitterIndexModel>): List<String> = elasticsearchOperations.bulkIndex(
        elasticIndexHelper.getIndexQueries(documents),
        IndexCoordinates.of(elasticConfigData.indexName)
    ).mapNotNull { it.id }.also {
        logger.info { "Documents indexed successfully with type: ${TwitterIndexModel::class.simpleName} and ids: $it" }
    }
}
