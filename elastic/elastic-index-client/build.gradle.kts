plugins {
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common-config-data"))
    implementation(project(":elastic:elastic-model"))

    implementation("org.springframework.boot:spring-boot-starter-data-elasticsearch")
}
