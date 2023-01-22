import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "1.7.0"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "me.zeepic"
version = "1.0"

repositories {
    mavenCentral()
    maven(url="https://papermc.io/repo/repository/maven-public/")
    maven(url="https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    implementation(kotlin("reflect"))
    implementation("org.reflections:reflections:0.10.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveClassifier.set("")
        archiveFileName.set("${bukkit.name}.jar")
        destinationDirectory.set(file("C:\\Users\\isaol\\Desktop\\Minecraft\\Paper 1.19 Server\\plugins"))

    }
}

bukkit {
    main = "me.zeepic.aiparkour.AIParkour"
    name = "AIParkour"
    version = "1.0.0"
    authors = listOf("ZeEpic")
    apiVersion = "1.19"
}
