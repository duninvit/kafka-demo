import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val springBootVersion: String by project

buildscript {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.springframework.boot") version "2.7.0" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.6.21" apply false
    kotlin("plugin.spring") version "1.6.21" apply false
}

allprojects {
    group = "com.github.duninvit"
    version = "0.0.1-SNAPSHOT"

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

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
    }

//    dependencies {
//        implementation("org.springframework.boot:spring-boot-starter")
//        implementation("org.jetbrains.kotlin:kotlin-reflect")
//        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
//        testImplementation("org.springframework.boot:spring-boot-starter-test")
//    }
}
