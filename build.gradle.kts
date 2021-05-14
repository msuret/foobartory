import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

project.setProperty("mainClassName", "com.example.foobartory.MainKt")

plugins {
    kotlin("jvm") version "1.5.0"
    application
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "com.example"
version = "0.0.1"
val mainClass1 = "com.example.foobartory.MainKt"

repositories {
    mavenCentral()
    jcenter()
    maven("https://repo.kotlin.link")
}

dependencies {
    implementation("space.kscience:plotlykt-server:0.4.0")
    implementation("org.slf4j:slf4j-simple:1.7.30")
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.0")
    testImplementation("io.mockk:mockk:1.11.0")
}

application {
    mainClass.set(project.findProperty("mainClassName") as? String)
}

tasks {
    test {
        useJUnitPlatform()
    }
    withType<KotlinCompile>() {
        kotlinOptions.jvmTarget = "11"
    }
    named<JavaExec>("run") {
        standardInput = System.`in`
    }
}