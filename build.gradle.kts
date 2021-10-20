import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.serialization.js.DynamicTypeDeserializer.id

plugins {
    id("java-gradle-plugin")
    kotlin("jvm") version "1.2.71"
}

apply(from= "publish.gradle")

group = "com.techbyflorin.android"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compile(gradleApi())

    compile(kotlin("stdlib-jdk8"))

    implementation("com.google.code.gson:gson:2.8.8")

    testCompile("junit", "junit", "4.12")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

gradlePlugin {
    plugins {

        // creating an unique id to ComposeBuildsPlugin implementation
        create("ComposeBuildsPlugin") {
            id = "com.techbyflorin.android.buildSrc.ComposeBuilds"
            implementationClass = "com.techbyflorin.android.buildSrc.plugins.ComposeBuildsPlugin"
        }
    }
}