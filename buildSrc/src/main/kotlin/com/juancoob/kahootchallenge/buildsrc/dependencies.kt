@file:Suppress("MatchingDeclarationName", "unused")

package com.juancoob.kahootchallenge.buildsrc

object Libs {

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.3.1"
    const val gradleVersionsBenManesPlugin = "com.github.ben-manes:gradle-versions-plugin:0.44.0"
    const val junit = "junit:junit:4.13.2"
    const val arrowCore = "io.arrow-kt:arrow-core:1.1.3"
    const val turbine = "app.cash.turbine:turbine:0.12.1"

    object Kotlin {
        private const val version = "1.7.22"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val kotlinxSerializationJson =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"

        object Coroutines {
            private const val version = "1.6.4"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.9.0"
        const val appCompact = "androidx.appcompat:appcompat:1.5.1"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val material = "com.google.android.material:material:1.7.0"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"

        object Activity {
            private const val version = "1.6.1"
            const val ktx = "androidx.activity:activity-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.5.1"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.4.3"
            const val roomRuntime = "androidx.room:room-runtime:$version"
            const val roomKtx = "androidx.room:room-ktx:$version"
            const val roomCompiler = "androidx.room:room-compiler:$version"
        }

        object Testing {
            const val runner = "androidx.test:runner:1.5.1"
            const val rules = "androidx.test:rules:1.5.0"

            object Ext {
                private const val version = "1.1.3"
                const val junitKtx = "androidx.test.ext:junit-ktx:$version"
            }

            object Espresso {
                private const val version = "3.4.0"
                const val contrib = "androidx.test.espresso:espresso-contrib:$version"
            }
        }
    }

    object GlideBumptech {
        private const val version = "4.14.2"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Retrofit {
        const val retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
        const val retrofit2KotlinxSerializationConverter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object OkHttp3 {
        private const val version = "4.10.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
        const val mockWebServer = "com.squareup.okhttp3:mockwebserver:$version"
    }

    object Hilt {
        private const val version = "2.44.2"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
        const val hiltAndroidTesting = "com.google.dagger:hilt-android-testing:$version"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-android-compiler:$version"
        const val hiltAndroidGradlePlugin = "com.google.dagger:hilt-android-gradle-plugin:$version"
    }

    object Javax {
        const val javaxInject = "javax.inject:javax.inject:1"
    }

    object Mockk {
        private const val version = "1.13.3"
        const val mockkLib = "io.mockk:mockk:$version"
        const val mockkAgentJvm = "io.mockk:mockk-agent-jvm:$version"
    }
}
