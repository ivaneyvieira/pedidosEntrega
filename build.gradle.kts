
import Build_gradle.Defs.vaadin10_version
import Build_gradle.Defs.vaadinonkotlin_version

object Defs {
  const val vaadinonkotlin_version = "1.0.2"
  const val vaadin10_version = "14.3.4"
  const val kotlin_version = "1.4.0"
  const val spring_boot_version = "2.2.6.RELEASE"
  const val vaadin_plugin = "0.8.0"
  //const val gretty_plugin = "3.0.1"
}

plugins {
  id("org.springframework.boot") version  "2.2.6.RELEASE"
  id("io.spring.dependency-management") version "1.0.9.RELEASE"
  kotlin("jvm") version "1.4.0"
  id("org.gretty") version "3.0.3"
  war
  id("com.vaadin") version "0.8.0"
  kotlin("plugin.spring") version "1.4.0"
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions.jvmTarget = "1.8"
}
group = "pedido"
version = "1.0"

dependencies {
  //Spring
  implementation("org.springframework.boot:spring-boot-starter-security")
  implementation("org.springframework.session:spring-session-core")
  providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
  // Vaadin-on-Kotlin dependency, includes Vaadin
  implementation("com.github.mvysny.karibudsl:karibu-dsl:${vaadinonkotlin_version}")
  // Vaadin 14
  implementation("com.vaadin:vaadin-core:${vaadin10_version}")
  implementation("com.vaadin:vaadin-spring-boot-starter:${vaadin10_version}")
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
  implementation("org.vaadin.stefan:lazy-download-button:1.0.0")
  implementation("com.github.nwillc:poink:0.4.6")
  implementation("com.flowingcode.addons:font-awesome-iron-iconset:2.1.2")
  implementation("org.vaadin.haijian:exporter:3.0.1")
  //compile("com.flowingcode.addons.applayout:app-layout-addon:2.0.2")
  implementation(kotlin("stdlib-jdk8"))
  
  implementation(kotlin("reflect"))
  // test support
  testImplementation("com.github.mvysny.kaributesting:karibu-testing-v10:1.1.16")
  testImplementation("com.github.mvysny.dynatest:dynatest-engine:0.15")
  // https://mvnrepository.com/artifact/net.sourceforge.dynamicreports/dynamicreports-core
  implementation("net.sourceforge.dynamicreports:dynamicreports-core:6.11.1") {
    exclude(group = "com.lowagie", module = "itext")
  }
  // https://mvnrepository.com/artifact/net.sf.jasperreports/jasperreports-fonts
  implementation("net.sf.jasperreports:jasperreports-fonts:6.12.2")
  
  implementation("com.lowagie:itext:2.1.7")
  implementation("javax.xml.bind:jaxb-api:2.3.1")
}

vaadin {
  productionMode = true
}

dependencyManagement {
  imports {
    mavenBom("com.vaadin:vaadin-bom:${vaadin10_version}")
  }
}

