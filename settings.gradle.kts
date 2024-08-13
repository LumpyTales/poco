import com.mooltiverse.oss.nyx.gradle.NyxExtension

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}

plugins {
    id("com.mooltiverse.oss.nyx") version "3.0.5"
}

rootProject.name = "poco"

include("core")
include("gradle-plugin")

extensions.configure<NyxExtension>("nyx") {
    preset = "simple"
    initialVersion = "0.0.0"

    releaseTypes.items.register("mainline") {
        filterTags = "^()?([0-9]\\d*)\\.([0-9]\\d*)\\.([0-9]\\d*)$"
        gitCommit = "true"
        gitCommitMessage = "Release {{version}}"
        gitTag = "true"
        gitTagMessage = "{{version}}"
        gitTagNames = listOf("{{version}}")
        matchBranches = "^(main)$"
    }
}