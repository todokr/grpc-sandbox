import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.4.2"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.google.protobuf") version "0.9.4"
    id("org.jooq.jooq-codegen-gradle") version "3.19.18"
    id("org.flywaydb.flyway") version "11.3.0"
}

group = "io.github.todokr"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

extra["springGrpcVersion"] = "0.3.0"

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.grpc:grpc-services")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.grpc:spring-grpc-spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("org.jooq:jooq")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    runtimeOnly("org.postgresql:postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.springframework.grpc:spring-grpc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    developmentOnly("org.springframework.boot:spring-boot-docker-compose")
    developmentOnly("org.jooq:jooq-codegen")
    developmentOnly("org.jooq:jooq-meta")
    jooqCodegen("org.postgresql:postgresql")
}

buildscript {
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:11.3.0")
    }
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.grpc:spring-grpc-dependencies:${property("springGrpcVersion")}")
    }
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java"
        }
    }
    // only generate server code
    generateProtoTasks {
        

        all().forEach {
            it.builtins {

            }
            it.plugins {
                id("grpc") {
                    option("jakarta_omit")
                    option("@generated=omit")
                    option("server_only=true")

                }
            }
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

val dbDriver = "org.postgresql.Driver"
val jdbcUrl = "jdbc:postgresql://localhost:5454/main"
val dbUser = "postgres"
val dbPass = "devtest"

flyway {
    driver = dbDriver
    url = jdbcUrl
    user = dbUser
    password = dbPass
    cleanDisabled = false
    locations = arrayOf("filesystem:db/migration")
}

jooq {
    configuration {
        jdbc {
            driver = dbDriver
            url = jdbcUrl
            user = dbUser
            password = dbPass
        }
        generator {
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "public"
                includes = ".*"
            }

            target {
                packageName = "io.github.todokr.jooq"
            }
        }
    }
}

sourceSets.main {
    java.srcDirs("build/generated-sources/jooq")
}