plugins {
    id("org.springframework.boot")
    id("com.bmuschko.docker-spring-boot-application")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    val springKafkaVersion: String by rootProject.extra
    val avroSerializerVersion: String by extra { "7.1.1" }
    implementation(project(":common-config-data"))
    implementation(project(":kafka:kafka-consumer"))
    implementation(project(":kafka:kafka-admin"))
    implementation(project(":kafka:kafka-model"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.cloud:spring-cloud-starter-config:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")
    implementation("io.confluent:kafka-avro-serializer:$avroSerializerVersion") {
        exclude(group = "org.slf4j", module = "slf4j-log4j12")
        exclude(group = "log4j", module = "log4j")
        exclude(group = "io.swagger", module = "swagger-annotations")
        exclude(group = "io.swagger", module = "swagger-core")
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

// Reference guide: https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#usage_3
docker {
    springBootApplication {
        baseImage.set("openjdk:17-jdk-alpine")
        images.set(setOf("${project.group}/kafka-to-elastic-service:${project.version}"))
    }
}
