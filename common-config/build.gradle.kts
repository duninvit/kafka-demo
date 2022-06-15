plugins {
    kotlin("plugin.spring")
}

dependencies {
    val springRetryVersion: String by rootProject.extra

    implementation(project(":common-config-data"))

//    implementation("org.springframework.boot:spring-boot-starter")
//    implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springframework.retry:spring-retry:$springRetryVersion")
}
