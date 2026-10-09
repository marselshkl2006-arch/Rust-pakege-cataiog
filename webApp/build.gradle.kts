import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.composeApp)
            implementation(libs.compose.ui)
            implementation(libs.ktor.client.js)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
    }
}
