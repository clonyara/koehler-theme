import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

group = providers.gradleProperty("pluginGroup").get()
version = providers.gradleProperty("pluginVersion").get()

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        create(
            IntelliJPlatformType.fromCode(providers.gradleProperty("platformType").get()),
            providers.gradleProperty("platformVersion").get(),
        )
        pluginVerifier()
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }
}

java {
    val v = JavaVersion.toVersion(providers.gradleProperty("javaVersion").get())
    sourceCompatibility = v
    targetCompatibility = v
}

intellijPlatform {
    pluginConfiguration {
        name = providers.gradleProperty("pluginName")
        version = providers.gradleProperty("pluginVersion")

        ideaVersion {
            sinceBuild = providers.gradleProperty("pluginSinceBuild")
            untilBuild = providers.gradleProperty("pluginUntilBuild")
        }
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

tasks {
    wrapper {
        gradleVersion = "8.10.2"
    }

    register("bumpVersion") {
        group = "versioning"
        description = "Bumps pluginVersion in gradle.properties. Usage: ./gradlew bumpVersion -Ppart=patch|minor|major"

        doLast {
            val part = (project.findProperty("part") as String? ?: "patch").lowercase()
            require(part in listOf("major", "minor", "patch")) {
                "part must be one of: major, minor, patch (got '$part')"
            }

            val propsFile = rootProject.file("gradle.properties")
            val lines = propsFile.readLines()
            val keyRegex = Regex("""^(\s*pluginVersion\s*=\s*)(\d+)\.(\d+)\.(\d+)\s*$""")

            var oldVersion: String? = null
            var newVersion: String? = null

            val updated = lines.map { line ->
                val m = keyRegex.matchEntire(line) ?: return@map line
                val (prefix, majS, minS, patS) = m.destructured
                var maj = majS.toInt(); var min = minS.toInt(); var pat = patS.toInt()
                when (part) {
                    "major" -> { maj++; min = 0; pat = 0 }
                    "minor" -> { min++; pat = 0 }
                    "patch" -> { pat++ }
                }
                oldVersion = "$majS.$minS.$patS"
                newVersion = "$maj.$min.$pat"
                "$prefix$newVersion"
            }

            checkNotNull(newVersion) { "pluginVersion=X.Y.Z not found in gradle.properties" }
            propsFile.writeText(updated.joinToString(System.lineSeparator(), postfix = System.lineSeparator()))
            logger.lifecycle("pluginVersion: $oldVersion -> $newVersion")
        }
    }
}
