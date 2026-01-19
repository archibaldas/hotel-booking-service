plugins {
	java
	id("org.springframework.boot") version "3.5.0"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
description = "A backend component for a hotel booking service with the ability to manage content through a CMS admin panel."

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.apache.commons:commons-csv:1.10.0")


    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")

    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

    compileOnly("org.projectlombok:lombok")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.projectlombok:lombok")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa-test")
	testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.awaitility:awaitility:4.2.0")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    testImplementation("net.javacrumbs.json-unit:json-unit:2.38.0")
    testImplementation("org.testcontainers:junit-jupiter:1.17.6")
    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("org.testcontainers:kafka")
    testImplementation("org.testcontainers:mongodb")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
