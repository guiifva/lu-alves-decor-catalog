val springdocVersion: String by project
val springMockkVersion: String by project

plugins {
	kotlin("jvm") version "2.2.20"
	kotlin("plugin.spring") version "2.2.20"
	id("org.springframework.boot") version "3.5.6"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "nocta"
version = "0.0.1-SNAPSHOT"
description = "lu-alves-decor-catalog"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(24)
	}

}

repositories {
	mavenCentral()
}

dependencies {
    implementation("org.flywaydb:flyway-core")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:$springdocVersion")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.postgresql:r2dbc-postgresql")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("com.ninja-squad:springmockk:$springMockkVersion")
    testImplementation("com.h2database:h2")
    testImplementation("io.r2dbc:r2dbc-h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
			jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
