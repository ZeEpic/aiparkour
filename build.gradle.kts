import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
}

group = "me.zeepic"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.google.code.gson:gson:2.10.1")
}

tasks {
    jar {
        destinationDirectory.set(file("C:\\Users\\isaol\\Desktop\\Minecraft\\Paper 1.19 Server\\plugins"))
        archiveFileName.set("AIParkour.jar")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
