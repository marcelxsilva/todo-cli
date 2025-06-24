plugins {
    id("application")
    kotlin("jvm") version "2.1.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.xerial:sqlite-jdbc:3.45.2.0")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("com.github.ajalt.clikt:clikt:3.5.2")
}

application {
    mainClass.set("org.example.MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "org.example.MainKt"
    }

    from(configurations.runtimeClasspath.get().map { zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

kotlin {
    jvmToolchain(23)
}