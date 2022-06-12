plugins {
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
