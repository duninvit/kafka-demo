plugins {
    id("com.bmuschko.docker-spring-boot-application")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    implementation(project(":common-config-data"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-autoconfigure:2.7.0")
    implementation("org.springframework.cloud:spring-cloud-config-server:3.1.3")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:3.1.3")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:3.0.4")
}

// Reference guide: https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#usage_3
docker {
    springBootApplication {
        baseImage.set("openjdk:17-jdk-alpine")
        images.set(setOf("${project.group}/config-server:${project.version}"))
    }
}
