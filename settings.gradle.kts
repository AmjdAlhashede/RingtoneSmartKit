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
include(":ringtone_smart_kit:di")
include(":ringtone_smart_kit:domain")
include(":ringtone_smart_kit:data")
