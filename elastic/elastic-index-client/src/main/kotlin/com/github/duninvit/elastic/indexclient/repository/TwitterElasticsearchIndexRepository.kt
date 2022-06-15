package com.github.duninvit.elastic.indexclient.repository

import com.github.duninvit.elastic.model.index.impl.TwitterIndexModel
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

@Repository
interface TwitterElasticsearchIndexRepository : ElasticsearchRepository<TwitterIndexModel, String> {
}
