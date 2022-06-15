import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion: String by extra { "1.6.21" }
val kotlinLoggingVersion: String by extra { "2.1.23" }
val jacksonModuleVersion: String by extra { "2.13.3" }
val springBootVersion: String by extra { "2.7.0" }
val springCloudVersion: String by extra { "3.1.3" }
val springKafkaVersion: String by extra { "2.8.6" }
val springRetryVersion: String by extra { "1.3.3" }
val springJasyptVersion: String by extra { "3.0.4" }
val avroVersion: String by extra { "1.11.0" }
val avroSerializerVersion: String by extra { "7.1.1" }
val twitter4jVersion: String by extra { "4.0.7" }
val httpclientVersion: String by extra { "4.5.13" }
val jsonVersion: String by extra { "20220320" }

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }
}

plugins {
    id("org.springframework.boot") version "2.7.0" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.6.21" apply false
    kotlin("plugin.spring") version "1.6.21" apply false
    id("com.bmuschko.docker-spring-boot-application") version "7.4.0" apply false
    id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0" apply false
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }

    group = "com.github.duninvit"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply {
        plugin("kotlin")
        plugin("org.jetbrains.kotlin.jvm")
        plugin("io.spring.dependency-management")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    dependencies {
        val implementation by configurations
        val testImplementation by configurations
        implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
        implementation("org.springframework.boot:spring-boot-starter-aop:$springBootVersion")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonModuleVersion")
        implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion")
        implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLoggingVersion")

        testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
