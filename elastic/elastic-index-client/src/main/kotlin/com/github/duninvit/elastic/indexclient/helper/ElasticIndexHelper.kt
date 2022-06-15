package com.github.duninvit.elastic.indexclient.helper

import com.github.duninvit.elastic.model.index.IndexModel
import org.springframework.data.elasticsearch.core.query.IndexQuery
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder
import org.springframework.stereotype.Component

@Component
class ElasticIndexHelper<T : IndexModel> {

    fun getIndexQueries(documents: List<T>): List<IndexQuery> =
        documents.map {
            IndexQueryBuilder()
                .withId(it.getId())
                .withObject(it)
                .build()
        }
}
