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
        maven { url = uri("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo/") }

    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }

        maven { url = uri("https://tencent-tds-maven.pkg.coding.net/repository/shiply/repo/") }

    }
}

rootProject.name = "Bilibili"
include(":app")
include(":api")
include(":common")
include(":db")
include(":repository")
include(":viewModel")
include(":tv")
