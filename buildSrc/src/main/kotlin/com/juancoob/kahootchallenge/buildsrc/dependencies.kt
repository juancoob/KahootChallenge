@file:Suppress("MatchingDeclarationName")

package com.juancoob.kahootchallenge.buildsrc

object Libs {

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val gradleVersionsBenManesPlugin = "com.github.ben-manes:gradle-versions-plugin:0.42.0"
    const val junit = "junit:junit:4.13.2"
    const val arrowCore = "io.arrow-kt:arrow-core:1.0.1"

    object Kotlin {
        private const val version = "1.7.0"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val kotlinxSerializationJson =
            "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2"

        object Coroutines {
            private const val version = "1.6.3"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.8.0"
        const val appCompact = "androidx.appcompat:appcompat:1.4.2"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.1"
        const val material = "com.google.android.material:material:1.6.1"
        const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.4"

        object Lifecycle {
            private const val version = "2.4.1"
            const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val runtimeKtx = "androidx.lifecycle:lifecycle-runtime-ktx:$version"
        }

        object Room {
            private const val version = "2.4.2"
            const val roomRuntime = "androidx.room:room-runtime:$version"
            const val roomKtx = "androidx.room:room-ktx:$version"
            const val roomCompiler = "androidx.room:room-compiler:$version"
        }

        object Testing {

            object Ext {
                private const val version = "1.1.3"
                const val junit = "androidx.test.ext:junit:$version"
            }

            object Espresso {
                private const val version = "3.4.0"
                const val contrib = "androidx.test.espresso:espresso-contrib:$version"
            }
        }
    }

    object GlideBumptech {
        private const val version = "4.13.2"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Retrofit {
        const val retrofit2 = "com.squareup.retrofit2:retrofit:2.9.0"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:4.9.3"
        const val retrofit2KotlinxSerializationConverter =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
    }

    object Hilt {
        private const val version = "2.42"
        const val hiltAndroid = "com.google.dagger:hilt-android:$version"
        const val hiltCompiler = "com.google.dagger:hilt-compiler:$version"
    }

    object Javax {
        const val javaxInject = "javax.inject:javax.inject:1"
    }

    object Mockk {
        private const val version = "1.12.4"
        const val mockkLib = "io.mockk:mockk:$version"
        const val mockkAgentJvm = "io.mockk:mockk-agent-jvm:$version"
    }
}
