plugins {
    java
    id("org.springframework.boot") version "3.5.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("io.freefair.lombok") version "9.1.0"
}

group = "care.cuddliness.djcarl"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:6.1.2")

    implementation("org.springframework.boot:spring-boot-starter")

    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.google.code.gson:gson:2.11.0")
    implementation("com.vdurmont:emoji-java:5.1.1")
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("one.stayfocused.spring:dotenv-spring-boot:1.0.0")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("com.microsoft.playwright:playwright:1.60.0")
}

tasks.test {
    useJUnitPlatform()
}