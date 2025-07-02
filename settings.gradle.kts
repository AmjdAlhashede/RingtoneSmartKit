@file:Suppress("UnstableApiUsage")

pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}


rootProject.name = "RingtoneSmartKit"
include(":app")
include(":ringtone_smart_kit")

include(":compose-demo")
project(":compose-demo").projectDir = File(rootDir, "examples/CompsoeDemo/compose-demo/")

include(":kotlin-xml-demo")
project(":kotlin-xml-demo").projectDir = File(rootDir, "examples/KotlinXmlDemo/kotlin-xml-demo/")

include(":java-demo")
project(":java-demo").projectDir = File(rootDir, "examples/JavaDemo/java-demo/")