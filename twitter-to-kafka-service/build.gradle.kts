plugins {
    id("org.springframework.boot")
    id("com.bmuschko.docker-spring-boot-application")

    kotlin("plugin.spring")
}

dependencies {
    val springCloudVersion: String by rootProject.extra
    val springJasyptVersion: String by rootProject.extra
    val avroVersion: String by rootProject.extra
    val twitter4jVersion: String by rootProject.extra
    val httpclientVersion: String by rootProject.extra
    val jsonVersion: String by rootProject.extra

    implementation(project(":common-config-data"))
    implementation(project(":kafka:kafka-admin"))
    implementation(project(":kafka:kafka-model"))
    implementation(project(":kafka:kafka-producer"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config:$springCloudVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:$springCloudVersion")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:$springJasyptVersion")

    implementation("org.twitter4j:twitter4j-stream:$twitter4jVersion")
    // For twitter v2 implementation
    implementation("org.apache.httpcomponents:httpclient:$httpclientVersion")
    implementation("org.json:json:$jsonVersion")
    implementation("org.apache.avro:avro:$avroVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Reference guide: https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#usage_3
docker {
    springBootApplication {
        baseImage.set("openjdk:17-jdk-alpine")
        images.set(setOf("${project.group}/twitter-to-kafka-service:${project.version}"))
    }
}
