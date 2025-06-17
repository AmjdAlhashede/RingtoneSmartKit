plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    `maven-publish`
    signing
}

group = "io.github.amjdalhashede"
version = "1.0.3-alpha"

android {
    namespace = "io.github.amjdalhashede"
    compileSdk = 36

    defaultConfig {
        minSdk = 22

        aarMetadata {
            minCompileSdk = 22
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    implementation(libs.bundles.dagger.bundle)
    kapt(libs.dagger.compiler)

    implementation(libs.androidx.lifecycle.runtime.ktx)
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                groupId = "io.github.amjdalhashede"
                artifactId = "ringtone-smart-kit"
                version = "1.0.3-alpha"

                pom {
                    name.set("Ringtone Smart Kit")
                    description.set("Android library to set ringtones from various sources.")
                    url.set("https://github.com/AmjdAlhashede/RingtoneSmartKitProject")

                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }

                    developers {
                        developer {
                            id.set("Amjdalhashede")
                            name.set("Amjd Alhashede")
                            url.set("https://github.com/AmjdAlhashede")
                        }
                    }

                    scm {
                        connection.set("scm:git:git://github.com/AmjdAlhashede/RingtoneSmartKitProject.git")
                        developerConnection.set("scm:git:ssh://git@github.com/AmjdAlhashede/RingtoneSmartKitProject.git")
                        url.set("https://github.com/AmjdAlhashede/RingtoneSmartKitProject")
                    }
                }
            }

        }

        repositories {
            maven {
                name = "myrepo"
                url = uri(layout.buildDirectory.dir("repo"))
            }

            maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/AmjdAlhashede/RingtoneSmartKitProject")
                credentials {
                    username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME_GITHUB")
                    password = project.findProperty("gpr.key") as String? ?: System.getenv("TOKEN_GITHUB")
                }
            }

            maven {
                name = "Sonatype"
                url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                credentials {
                    username = project.findProperty("ossrhUsername")  as String?
                    password = project.findProperty("ossrhPassword") as String?
                }
            }
        }
    }

    signing {
        useGpgCmd()
        sign(publishing.publications["release"])
    }
}


tasks.register<Zip>("generateRepoZip") {
    dependsOn("publishReleasePublicationToMyrepoRepository")

    from(layout.buildDirectory.dir("repo"))

    archiveFileName.set("ringtone_smart_kit-1.0.3-alpha.zip")
    destinationDirectory.set(layout.buildDirectory.dir("outputs"))
}

tasks.register("publishToMavenCentral") {
    dependsOn("publishReleasePublicationToSonatypeRepository", "closeAndReleaseSonatypeStagingRepository")
}

