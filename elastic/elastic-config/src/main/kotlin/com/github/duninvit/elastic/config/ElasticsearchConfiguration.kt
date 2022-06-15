package com.github.duninvit.elastic.config

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.json.jackson.JacksonJsonpMapper
import co.elastic.clients.transport.rest_client.RestClientTransport
import com.github.duninvit.common.configdata.ElasticConfigData
import org.apache.http.HttpHost
import org.elasticsearch.client.RestClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.ElasticsearchOperations
import org.springframework.data.elasticsearch.core.convert.ElasticsearchConverter
import org.springframework.data.elasticsearch.core.convert.MappingElasticsearchConverter
import org.springframework.data.elasticsearch.core.mapping.SimpleElasticsearchMappingContext
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories
import org.springframework.web.util.UriComponentsBuilder


@Configuration
@EnableElasticsearchRepositories(basePackages = ["com.github.duninvit.elastic.indexclient.repository"])
class ElasticsearchConfiguration(
    private val elasticConfigData: ElasticConfigData
) {

    @Bean
    fun elasticsearchClient(): ElasticsearchClient {
        val serverUri = UriComponentsBuilder
            .fromHttpUrl(elasticConfigData.connectionUrl)
            .build()
        val restClient = RestClient.builder(
            HttpHost(
                serverUri.host,
                serverUri.port,
                serverUri.scheme,
            )
        ).setRequestConfigCallback {
            it.setConnectTimeout(elasticConfigData.connectionTimeoutMs)
                .setSocketTimeout(elasticConfigData.socketTimeoutMs)
        }.build()
        return ElasticsearchClient(RestClientTransport(restClient, JacksonJsonpMapper()))
    }

    @Bean
    fun elasticsearchMappingContext(): SimpleElasticsearchMappingContext = SimpleElasticsearchMappingContext()

    @Bean
    fun elasticsearchConverter(): ElasticsearchConverter = MappingElasticsearchConverter(elasticsearchMappingContext())

    @Bean
    fun elasticsearchTemplate(): ElasticsearchOperations =
        ElasticsearchTemplate(elasticsearchClient(), elasticsearchConverter())
}
