plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    val springKafkaVersion: String by rootProject.extra
    val avroSerializerVersion: String by extra { "7.1.1" }
    implementation(project(":common-config-data"))
    implementation(project(":kafka:kafka-model"))

    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
    implementation("io.confluent:kafka-avro-serializer:$avroSerializerVersion") {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "log4j", module = "log4j")
        exclude(group = "io.swagger", module = "swagger-annotations")
        exclude(group = "io.swagger", module = "swagger-core")
    }
}

