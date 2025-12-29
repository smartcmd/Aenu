plugins {
    id("java-library")
    id("org.allaymc.gradle.plugin") version "0.2.1"
}

group = "me.daoge.aenu"
description = "A configurable menu plugin for AllayMC with PlaceholderAPI support"
version = "0.2.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

allay {
    api = "0.20.0-SNAPSHOT"

    plugin {
        entrance = ".Aenu"
        authors += "daoge_cmd"
        website = "https://github.com/smartcmd/Aenu"
        dependency("PlaceholderAPI")
    }
}

dependencies {
    compileOnly(group = "org.projectlombok", name = "lombok", version = "1.18.34")
    compileOnly(group = "org.allaymc", name = "papi", version = "0.1.2")
    compileOnly(group = "org.yaml", name = "snakeyaml", version = "2.2")
    annotationProcessor(group = "org.projectlombok", name = "lombok", version = "1.18.34")
}
