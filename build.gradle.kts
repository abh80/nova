plugins {
    java
    scala
    application
}

group = "org.plat.flowops"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
object Libs {
    const val SCALA_LIBRARY = "org.scala-lang:scala3-library_3:3.3.0"
    const val JUNIT = "org.junit.jupiter:junit-jupiter:5.7.0"
    const val SCALA_TEST = "org.scalatest:scalatest_3:3.2.18"
    const val JGIT_LIBRARY = "org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r"
    const val JGIT_HTTP = "org.eclipse.jgit:org.eclipse.jgit.http.server:6.8.0.202311291450-r"
    const val SCALA_LOGGING = "com.typesafe.scala-logging:scala-logging_3:3.9.5"
    const val SLF4J_API = "org.slf4j:slf4j-api:2.0.12"
    const val SLF4J_SIMPLE = "org.slf4j:slf4j-simple:2.0.12"
    const val SERVLET_API = "javax.servlet:javax.servlet-api:4.0.1"
    const val JETTY_SERVER = "org.eclipse.jetty:jetty-server:12.0.6"
    const val JETTY_WEBAPP = "org.eclipse.jetty:jetty-webapp:10.0.17"
    const val LOGBACK_CLASSIC = "ch.qos.logback:logback-classic:1.5.0"
    const val JOBRUNR = "org.jobrunr:jobrunr:6.3.5"
    const val JDBC_POSTGRES = "org.postgresql:postgresql:42.7.2"
    const val SPRING_VAULT_CORE = "org.springframework.vault:spring-vault-core:3.1.1"
    const val SCALA_MOCK = "org.scalamock:scalamock_3:6.0.0"
    const val SCALACTIC = "org.scalactic:scalactic_3:3.2.18"
}

dependencies {
    implementation(Libs.SCALA_LIBRARY)
    implementation(Libs.JGIT_LIBRARY)
    implementation(Libs.SCALA_LOGGING)
    implementation(Libs.SLF4J_API)
    implementation(Libs.SERVLET_API)
    implementation(Libs.LOGBACK_CLASSIC)
    implementation(Libs.JETTY_WEBAPP)
    implementation(Libs.JGIT_HTTP)
    implementation(Libs.JOBRUNR)
    implementation(Libs.JDBC_POSTGRES)
    implementation(Libs.SPRING_VAULT_CORE)
    testImplementation(Libs.SCALA_TEST)
    testImplementation(Libs.SCALA_MOCK)
    testImplementation(Libs.SCALACTIC)
    testRuntimeOnly("org.junit.platform:junit-platform-engine:1.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher:1.10.0")
    testRuntimeOnly("org.scalatestplus:junit-5-10_3:3.2.18.0")
}

tasks.test {
    useJUnitPlatform {
        includeEngines("scalatest")
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
    environment("TEST_VAR", "test_value")
}