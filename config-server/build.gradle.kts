plugins {
    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common-config-data"))

    implementation("org.springframework.cloud:spring-cloud-config-server:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.0")
}
