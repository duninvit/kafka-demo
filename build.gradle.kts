import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion by extra { "1.6.21" }
val kotlinLoggingVersion by extra { "2.1.23" }
val jacksonModuleVersion by extra { "2.13.3" }
val springBootVersion by extra { "2.7.0" }
val springKafkaVersion by extra { "2.8.6" }
val springRetryVersion by extra { "1.3.3" }

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
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21" apply false
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

subprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven {
            url = uri("https://packages.confluent.io/maven/")
        }
    }

    group = "com.github.duninvit"
    version = "0.0.1-SNAPSHOT"

    apply {
        plugin("io.spring.dependency-management")
        plugin("kotlin")
    }

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    dependencies {
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
