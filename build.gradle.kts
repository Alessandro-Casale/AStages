import me.modmuss50.mpp.ReleaseType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
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
    compileOnly("curse.maven:roughly-enough-items-310111:6199140")

    implementation("curse.maven:in-control-257356:5932871")

    implementation("curse.maven:fastworkbench-288885:5670423")
    implementation("curse.maven:placebo-283644:6105436")

    compileOnly("curse.maven:toxony-1236984:6811991")
    compileOnly("curse.maven:pagans-blessing-952071:5817130")

    implementation("curse.maven:sodium-394468:6382651")

    compileOnly("curse.maven:modernfix-790626:7509726")

    implementation("curse.maven:geckolib-388172:7707149")
    implementation("curse.maven:born-in-chaos-686437:7366499")

    implementation("curse.maven:puzzles-lib-495476:7140307")
    implementation("curse.maven:ender-zoology-857968:7021585")
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

publishMods {
    file.set(tasks.jar.flatMap { it.archiveFile })
    modLoaders.add("neoforge")
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    val formattedDate: String = today.format(formatter)
    val changelogFile = layout.projectDirectory.file("CHANGELOG.md")
    val formattedVersion = mod_version.substringBeforeLast("-")

    when {
        mod_version.contains("alpha", true) -> {
            type.set(ALPHA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is an alpha version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        mod_version.contains("beta", true) -> {
            type.set(BETA)
            changelog.set(
                """
                        ## [$formattedVersion] - $formattedDate
                        This is a beta version meant to be used only by developers!   
                        Changelog can be found in Discord server.
                    """.trimIndent()
            )
        }
        else -> {
            type.set(STABLE)
            changelog.set(providers.fileContents(changelogFile).asText.orElse("No changelog provided."))
        }
    }

    github {
        accessToken.set(providers.environmentVariable("GITHUB_TOKEN"))
        repository.set("Alessandro-Casale/AStages")
        val version = mod_version.substringBeforeLast("-")
        val branch = mod_version.substringAfterLast("-")
        commitish.set(branch.toMcRange())
        tagName.set("v$mod_version")

        displayName.set("AStages $mod_version")

        announcementTitle.set("Download from GitHub")
    }

    curseforge {
        accessToken.set(providers.environmentVariable("CURSEFORGE_API_KEY"))
        projectId.set("1120180")
        minecraftVersions.add(minecraft_version)
        changelogType.set("markdown")
        optional(
            "roughly-enough-items", "jei", "kubejs",
            "in-control", "jade", "fastworkbench"
        )

        displayName.set("astages-$mod_version")

        projectSlug.set("astages") // For discord setup
        announcementTitle.set("Download from CurseForge") // For discord setup
    }

    modrinth {
        accessToken.set(providers.environmentVariable("MODRINTH_API_KEY"))
        projectId.set("6wy8fmIk")
        minecraftVersions.add(minecraft_version)
        optional(
            "rei", "jei", "kubejs",
            "in-control", "jade"
        )

        displayName.set("astages-$mod_version")

        if (type.get() == ReleaseType.STABLE) {
            changelog.set(
                providers.fileContents(changelogFile)
                    .asText
                    .map { it.lineSequence().drop(3).joinToString("\n") }
            )
        } else {
            changelog.set(changelog.get().dropFirstLine())
        }

        announcementTitle.set("Download from Modrinth")
    }

//    discord {
//        webhookUrl.set(providers.environmentVariable("DISCORD_WEBHOOK"))
//        username.set("AServer")
//        avatarUrl.set(logoLocation)
//        content.set(changelog)
//        setPlatforms(publishMods.platforms["curseforge"], publishMods.platforms["modrinth"])
//
//        style {
//            thumbnailUrl = logoLocation
//            look = "MODERN"
//            link = "BUTTON"
//        }
//    }
}

fun String.toMcRange(): String {
    return this.substringBeforeLast(".") + ".X"
}

fun String.dropFirstLine(): String {
    return lines().drop(1).joinToString("\n")
}