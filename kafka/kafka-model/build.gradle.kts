import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask

plugins {
    id("com.github.davidmc24.gradle.plugin.avro")
}

dependencies {
    val avroVersion: String by rootProject.extra

    implementation(project(":common-config"))

    implementation("org.apache.avro:avro:$avroVersion")
}

avro {
    isCreateSetters.set(true)
    isCreateOptionalGetters.set(false)
    isGettersReturnOptional.set(false)
    isOptionalGettersForNullableFieldsOnly.set(false)
    fieldVisibility.set("PRIVATE")
    outputCharacterEncoding.set("UTF-8")
    stringType.set("String")
    templateDirectory.set(null as String?)
    isEnableDecimalLogicalType.set(true)
}

val generateAvro = tasks.withType<GenerateAvroJavaTask> {
    source("${projectDir}/src/main/resources/avro")
    setOutputDir(file("${projectDir}/src/main/java"))
}

tasks.named("compileJava").configure {
    dependsOn(generateAvro)
}

tasks.named("compileKotlin").configure {
    dependsOn(generateAvro)
}
