import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.4.0-SNAPSHOT"
	id("io.spring.dependency-management") version "1.0.10.RELEASE"
	kotlin("jvm") version "1.4.0"
	kotlin("plugin.spring") version "1.4.0"
	kotlin("kapt") version "1.4.10"
}

apply(plugin = "kotlin-kapt")

group = "com.udemy.course.kafka.twitter"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	val arrowVersion = "0.11.0"
	val kotlinLogginVersion = "2.0.2"
	val typeSafeConfigVersion = "1.4.0"

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")

	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

	// logging
	implementation("io.github.microutils:kotlin-logging-jvm:$kotlinLogginVersion")

	// kafka
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.apache.kafka:kafka-streams")

	// type safe
	implementation("com.typesafe:config:$typeSafeConfigVersion")

	// arrow
	implementation("io.arrow-kt:arrow-fx:$arrowVersion")
	implementation("io.arrow-kt:arrow-optics:$arrowVersion")
	implementation("io.arrow-kt:arrow-syntax:$arrowVersion")
	implementation("io.arrow-kt:arrow-fx-reactor:$arrowVersion")
	kapt("io.arrow-kt:arrow-meta:$arrowVersion")
	implementation("io.arrow-kt:arrow-mtl:$arrowVersion")


	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}
