plugins {
	java
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
}

group = "be.kdg.integration3"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("com.google.code.gson:gson:2.10.1")
	implementation("org.webjars:bootstrap:5.3.2")
	implementation("org.webjars:webjars-locator-core:0.48")
	implementation("com.fazecast:jSerialComm:2.10.3")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
