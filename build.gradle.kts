@Suppress("PropertyName") val mod_group_id: String by project
@Suppress("PropertyName") val mod_id: String by project
@Suppress("PropertyName") val mod_version: String by project
@Suppress("PropertyName") val mod_name: String by project
@Suppress("PropertyName") val mod_license: String by project
@Suppress("PropertyName") val mod_authors: String by project
@Suppress("PropertyName") val mod_description: String by project

@Suppress("PropertyName") val minecraft_version: String by project
@Suppress("PropertyName") val neo_version: String by project
@Suppress("PropertyName") val parchment_mappings_version: String by project
@Suppress("PropertyName") val parchment_minecraft_version: String by project
@Suppress("PropertyName") val kubejs_version: String by project
@Suppress("PropertyName") val minecraft_version_range: String by project
@Suppress("PropertyName") val loader_version_range: String by project
@Suppress("PropertyName") val neo_version_range: String by project

plugins {
    id("java-library")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.41-beta"
}

version = mod_version
group = mod_group_id

repositories {
    mavenLocal()
}

base {
    archivesName = mod_id
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    version = project.property("neo_version") as String

    parchment {
        mappingsVersion = project.property("parchment_mappings_version") as String
        minecraftVersion = project.property("parchment_minecraft_version") as String
    }

    runs {
        create("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("server") {
            server()
            programArgument("--nogui")

            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("gameTestServer") {
            type = "gameTestServer"

            systemProperty("neoforge.enabledGameTestNamespaces", mod_id)
        }

        create("data") {
            data()

            programArguments.addAll(
                "--mod", mod_id,
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create(project.property("mod_id") as String) {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets.main {
    resources.srcDir("src/generated/resources")
}

repositories {
    maven {
        url = uri("https://cursemaven.com")
    }

    maven {
        url = uri("https://maven.architectury.dev")
        content {
            includeGroup("dev.architectury")
        }
    }

    maven {
        url = uri("https://maven.latvians.dev/releases")
        content {
            includeGroup("dev.latvian.mods")
        }
    }

    maven {
        url = uri("https://maven.neoforged.net/releases")
    }

    maven {
        url = uri("https://maven.architectury.dev/")
    }

    maven {
        url = uri("https://maven.latvian.dev/releases")
        content {
            includeGroup("dev.latvian.mods")
            includeGroup("dev.latvian.apps")
        }
    }

    maven {
        url = uri("https://maven.blamejared.com")
        content {
            includeGroup("mezz.jei")
            includeGroup("net.darkhax.bookshelf")
            includeGroup("net.darkhax.gamestages")
        }
    }

    maven {
        url = uri("https://jitpack.io")
        content {
            includeGroup("com.github.rtyley")
        }
    }
}

dependencies {
    implementation("dev.latvian.mods:kubejs-neoforge:$kubejs_version")
    implementation("curse.maven:probejs-585406:7105159")

    implementation("curse.maven:jade-324717:5976517")
    implementation("curse.maven:jei-238222:7229074")

    implementation("curse.maven:in-control-257356:5932871")

    implementation("curse.maven:fastworkbench-288885:5670423")
    implementation("curse.maven:placebo-283644:6105436")

    implementation("curse.maven:toxony-1236984:6811991")
    implementation("curse.maven:pagans-blessing-952071:5817130")

    implementation("curse.maven:sodium-394468:6382651")
}

val generateModMetadata = tasks.register<ProcessResources>("generateModMetadata") {
    val replaceProperties = mapOf(
        "minecraft_version" to minecraft_version,
        "minecraft_version_range" to minecraft_version_range,
        "neo_version" to neo_version,
        "neo_version_range" to neo_version_range,
        "loader_version_range" to loader_version_range,
        "mod_id" to mod_id,
        "mod_name" to mod_name,
        "mod_license" to mod_license,
        "mod_version" to mod_version,
        "mod_authors" to mod_authors,
        "mod_description" to mod_description
    )

    inputs.properties(replaceProperties)
    expand(replaceProperties)

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

sourceSets {
    named("main") {
        resources {
            srcDir(generateModMetadata)
        }
    }
}

neoForge {
    ideSyncTask(generateModMetadata)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}
