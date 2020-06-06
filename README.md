# Introduction

This is a gradle plugin that packages Nifi processor projects
into a \*.nar archive. The project has one task named **nar** that extends the Gradle Jar task. The project is based wholey on the work of [Robert Kuhne](https://github.com/sponiro/gradle-nar-plugin). The code is the same, the only tangible difference is that the original plugin did not package the archive up as a nar file. Instead, it made 2 archives: a jar file containing all of the code, and a nar file containing the manifest and dependences. Additionally, the project was languishing on Github and was thus not availabe to the Gradle community. The project was written in Groovy and in addition to having JUnit tests, makes use of the Spock testing framework to perform integration tests.

The plugin manages the creation of all of the manifest entries and packaging of all of the dependencies for the Nifi processor. The processor’s dependencies are collected and placed into the META-INF/bundled-dependencies folder. The plugin creates manifest entries (*MANIFEST.MF*) for the plugin that are required by Nifi.

| Manifest Property Key  | Value              |
| ---------------------- | ------------------ |
| Nar-Group              | project.group      |
| Nar-Id                 | project.name       |
| Nar-Version            | project.version    |
| Nar-Dependency-Group   | nar config group   |
| Nar-Dependency-Id      | nar config name    |
| Nar-Dependency-Version | nar config Version |

Default Manifest Properties

The values for the manifest are derived from the Gradle project.
The last 3 manifest entries are dynamically created by the plugin.
All of the values can be overriden by including a configuration block in the build.gradle file:

**build.gradle configuration.**

    nar {
        manifest {
            attributes 'Nar-Group' : 'some value'
        }
    }

# Building

Executing `./gradlew` builds and assembles the project
Executing `./gradlew publishToMavenLocal` creates all of the artifacts and pushes them to your local maven repository.

# Using


plugins {
  id "me.ragill:nar-gradle-plugin:${narGradlePluginVersion}"
}

**Minimal Buildscript block.**

    buildscript {
        repositories {
          jcenter()
        }
        dependencies {
            classpath (
                "me.ragill:nar-gradle-plugin:${narGradlePluginVersion}"
            )
        }
    }
    apply plugin: 'nar-plugin'

The buildscript{} block must be located at the top of a build.gradle file and must include the
repositories where the plugin is located and the plugin as a classpath dependency. After the buildscript{} block,
the plugin needs to appear in an *apply plugin:* listing.

# TODO

-   Upgrade Java version and make into a modular project
-   Upgrade to Groovy 3+ once Spock has been upgraded
