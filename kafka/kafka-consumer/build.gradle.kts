plugins {
    kotlin("plugin.spring")
}

dependencies {
    val springKafkaVersion: String by rootProject.extra
    val avroVersion: String by rootProject.extra

    implementation(project(":common-config-data"))

    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
    implementation("org.apache.avro:avro:$avroVersion")
}
