plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":kafka:kafka-admin"))
    implementation(project(":kafka:kafka-model"))
    implementation(project(":kafka:kafka-producer"))
}
