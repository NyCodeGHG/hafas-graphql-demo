import com.apollographql.apollo3.gradle.internal.ApolloDownloadSchemaTask
import org.jetbrains.compose.compose

plugins {
    kotlin("js") version "1.7.0"
    id("org.jetbrains.compose") version "1.2.0-alpha01-dev755"
    id("com.apollographql.apollo3").version("3.5.0")
}

group = "dev.nycode"
version = "1.0.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }
}

dependencies {
    implementation(compose.web.core)
    implementation(compose.runtime)
    implementation("com.apollographql.apollo3:apollo-runtime:3.5.0")
}

apollo {
    packageName.set("dev.nycode.hafas_graphql_demo")
}
