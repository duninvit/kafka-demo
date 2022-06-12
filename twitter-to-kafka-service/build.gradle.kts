plugins {
    id("org.springframework.boot")
    id("com.bmuschko.docker-spring-boot-application")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common-config-data"))
    implementation(project(":kafka:kafka-admin"))
    implementation(project(":kafka:kafka-model"))
    implementation(project(":kafka:kafka-producer"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")

    implementation("org.twitter4j:twitter4j-stream:4.0.7")
    // For twitter v2 implementation
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.json:json:20220320")
    implementation("org.apache.avro:avro:1.11.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Reference guide: https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#usage_3
docker {
    springBootApplication {
        baseImage.set("openjdk:17")
        images.set(setOf("${project.group}/twitter-to-kafka-service:${project.version}"))
    }
}
