import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val vaadinonkotlin_version = "1.0.1"
val vaadin10_version = "14.2.0"
val kotlin_version = "1.3.71"
val spring_boot_version = "2.1.0.RELEASE"

plugins {
  id("org.springframework.boot") version "2.2.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  kotlin("jvm") version "1.3.71"
  id("org.gretty") version "3.0.1"
  war
  id("com.vaadin") version "0.6.0"
  kotlin("plugin.spring") version "1.3.71"
}

defaultTasks("clean", "vaadinBuildFrontend", "build")

repositories {
  mavenCentral()
  jcenter() // for Gretty runners
  maven {
    url = uri("https://maven.vaadin.com/vaadin-addons")
  }
}

gretty {
  contextPath = "/"
  servletContainer = "jetty9.4"
}
val staging by configurations.creating

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}

group = "pedidoEntrega"
version = "1.0"

dependencies {
  //Spring
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.session:spring-session-core")
  providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
  // Vaadin-on-Kotlin dependency, includes Vaadin
  implementation("com.github.mvysny.karibudsl:karibu-dsl:$vaadinonkotlin_version")
  // Vaadin 14
  implementation("com.vaadin:vaadin-core:$vaadin10_version")
  implementation("com.vaadin:vaadin-spring-boot-starter:$vaadin10_version")
  providedCompile("javax.servlet:javax.servlet-api:3.1.0")
  
  implementation("com.zaxxer:HikariCP:3.4.1")
  // logging
  // currently we are logging through the SLF4J API to LogBack. See src/main/resources/logback.xml file for the logger configuration
  implementation("ch.qos.logback:logback-classic:1.2.3")
  implementation("org.slf4j:slf4j-api:1.7.30")
  //implementation("org.slf4j:slf4j-simple:1.7.30")
  implementation("org.sql2o:sql2o:1.6.0")
  implementation("mysql:mysql-connector-java:5.1.48")
  implementation("com.zaxxer:HikariCP:3.4.1")
  implementation("org.imgscalr:imgscalr-lib:4.2")
  implementation("com.jcraft:jsch:0.1.55")
  implementation("org.cups4j:cups4j:0.7.6")
  // logging
  // currently we are logging through the SLF4J API to SLF4J-Simple. See src/main/resources/simplelogger.properties file for the logger configuration
  //implementation("com.github.appreciated:app-layout-addon:3.0.0.beta5")
  implementation("org.vaadin.tatu:twincolselect:1.2.0")
  implementation("org.vaadin.gatanaso:multiselect-combo-box-flow:1.1.0")
  implementation("org.vaadin.tabs:paged-tabs:2.0.1")
  implementation("org.claspina:confirm-dialog:2.0.0")
  //  compile("org.webjars.bowergithub.vaadin:vaadin-combo-box:4.2.7")
  //compile("com.github.appreciated:app-layout-addon:4.0.0.rc4")
  implementation("org.vaadin.crudui:crudui:4.1.0")
  //compile("com.flowingcode.addons.applayout:app-layout-addon:2.0.2")
  implementation(kotlin("stdlib-jdk8"))
  implementation(kotlin("reflect"))
  //compile("org.jetbrains.kotlin:kotlin-reflect")
  // test support
  testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.1.16")
  testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.15")
}

vaadin {
  productionMode = true
}

dependencyManagement {
  imports {
    mavenBom("com.vaadin:vaadin-bom:$vaadin10_version")
  }
}

