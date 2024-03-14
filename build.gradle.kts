group = "com.github.sirblobman.discord"
version = "1.0.0"

plugins {
    id("java")
    id("distribution")
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
    maven("https://m2.dv8tion.net/releases/")
}

dependencies {
    // JDA
    implementation("net.dv8tion:JDA:5.0.0-beta.21") {
        exclude(module = "opus-java") // Remove Opus as we don't handle voice/sound
    }

    // Other Dependencies
    compileOnly("org.jetbrains:annotations:24.0.1") // JetBrains Annotations
    implementation("org.apache.logging.log4j:log4j-slf4j2-impl:2.21.1") // Log4J SLF4J2 Implementation
}

distributions {
    main {
        contents {
            into("/target") {
                from(tasks.named("jar")) // Main Jar File
                from(configurations["runtimeClasspath"]) // Dependency Jar Files
            }
        }
    }
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-Xlint:deprecation")
        options.compilerArgs.add("-Xlint:unchecked")
    }

    named<Jar>("jar") {
        archiveBaseName.set("JDA-Template-Bot")
        val mainClassName = "com.github.sirblobman.discord.template.TemplateMain"
        val manifestDependencies = configurations.runtimeClasspath.get().joinToString(" ") { it.name }

        manifest {
            attributes["Main-Class"] = mainClassName
            attributes["Class-Path"] = manifestDependencies
        }
    }

    named<Tar>("distTar") {
        compression = Compression.GZIP
        archiveExtension.set("tar.gz")
        archiveBaseName.set("JDA-Template-Bot")
    }

    named<Zip>("distZip") {
        enabled = false
    }
}