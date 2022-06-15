package com.github.duninvit.elastic.indexclient.service

import com.github.duninvit.elastic.model.index.IndexModel

interface ElasticIndexClient<T : IndexModel> {

    fun save(documents: List<T>): List<String>
}
