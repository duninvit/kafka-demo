plugins {
    kotlin("plugin.spring")
}

dependencies {
    val springBootVersion: String by rootProject.extra
    val springKafkaVersion: String by rootProject.extra
    val springRetryVersion: String by rootProject.extra

    implementation(project(":common-config-data"))

    implementation("org.springframework.boot:spring-boot-starter-webflux:$springBootVersion")
    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
    implementation("org.springframework.retry:spring-retry:$springRetryVersion")
}
