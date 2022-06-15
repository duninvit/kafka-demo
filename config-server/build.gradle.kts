plugins {
    id("com.bmuschko.docker-spring-boot-application")

    kotlin("plugin.spring")
}

dependencies {
    val springBootVersion: String by rootProject.extra
    val springCloudVersion: String by rootProject.extra
    val springJasyptVersion: String by rootProject.extra

    implementation(project(":common-config-data"))

    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-autoconfigure:$springBootVersion")
    implementation("org.springframework.cloud:spring-cloud-config-server:$springCloudVersion")
    implementation("org.springframework.cloud:spring-cloud-starter-bootstrap:$springCloudVersion")
    implementation("com.github.ulisesbocchio:jasypt-spring-boot-starter:$springJasyptVersion")
}

// Reference guide: https://bmuschko.github.io/gradle-docker-plugin/current/user-guide/#usage_3
docker {
    springBootApplication {
        baseImage.set("openjdk:17-jdk-alpine")
        images.set(setOf("${project.group}/config-server:${project.version}"))
    }
}
