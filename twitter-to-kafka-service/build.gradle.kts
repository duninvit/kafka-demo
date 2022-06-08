plugins {
    id("org.springframework.boot")

    kotlin("jvm")
    kotlin("plugin.spring")
}

dependencies {
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.23")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.twitter4j:twitter4j-stream:4.0.7")
    // For twitter v2 implementation
    implementation("org.apache.httpcomponents:httpclient:4.5.13")
    implementation("org.json:json:20220320")

    implementation(project(":common-config"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}
