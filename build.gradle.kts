import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.kotlin.dsl.filter

plugins {
    kotlin("jvm") version "2.2.20"
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.5"
}

group = "dev.lumas.jobsaddons"
version = "2.1"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://storehouse.okaeri.eu/repository/maven-public/")
    maven("https://repo.jsinco.dev/releases")
    maven("https://jitpack.io")
    maven("https://maven.enginehub.org/repo/")
    maven("https://jitpack.io")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    compileOnly("dev.lumas.lumacore:LumaCore:d56563b")
    compileOnly("com.github.Zrips:jobs:v4.17.2")
    compileOnly("net.luckperms:api:5.4")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.9-beta1")
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.047-CUSTOM") {
        isTransitive = false
    }
    implementation("eu.okaeri:okaeri-configs-yaml-bukkit:5.0.5")
    implementation("eu.okaeri:okaeri-configs-serdes-bukkit:5.0.5")
    implementation("com.zaxxer:HikariCP:6.3.0")
    testImplementation(kotlin("test"))

    compileOnly("de.Linus122.SafariNet:SafariNet:1.16.29-SNAPSHOT")
    compileOnly("com.oheers.fish:even-more-fish:2.1.5-SNAPSHOT-1.21-RELEASE")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        isTransitive = false
    }
    compileOnly("me.clip:placeholderapi:2.11.6")
}

kotlin {
    jvmToolchain(21)
}

tasks {

    build {
        dependsOn(shadowJar)
    }

    jar {
        enabled = false
    }

    shadowJar {
        relocate("eu.okaeri", "dev.lumas.jobsaddons.libs.okaeri")
        relocate("com.zaxxer.hikari", "dev.lumas.jobsaddons.libs.hikari")
        archiveClassifier.set("")
    }

    runServer {
        downloadPlugins {
            modrinth("worldedit", "3ISh7ADm")
            modrinth("worldguard", "7.0.14")
            modrinth("luckperms", "v5.5.17-bukkit")
            url("https://github.com/Zrips/CMILib/releases/download/1.5.7.0/CMILib1.5.7.0.jar")
            url("https://github.com/Zrips/Jobs/releases/download/v5.2.6.3/Jobs5.2.6.3.jar")
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
            url("https://ci.ender.zone/job/EssentialsX/lastSuccessfulBuild/artifact/jars/EssentialsX-2.22.0-dev+48-b0ef568.jar")
            modrinth("placeholderapi", "2.11.7")
        }
        minecraftVersion("1.21.10")
    }

    processResources {
        outputs.upToDateWhen { false }
        filter<ReplaceTokens>(mapOf(
            "tokens" to mapOf("version" to project.version.toString()),
            "beginToken" to "\${",
            "endToken" to "}"
        )).filteringCharset = "UTF-8"
    }

    test {
        useJUnitPlatform()
    }
}