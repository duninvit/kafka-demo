plugins {
    kotlin("plugin.spring")
}

dependencies {
    val elasticsearchVersion: String by rootProject.extra

    implementation(project(":common-config-data"))

    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
    implementation("org.springframework:spring-web")
    implementation("org.elasticsearch:elasticsearch:$elasticsearchVersion")
    implementation("co.elastic.clients:elasticsearch-java:$elasticsearchVersion")
}
