@file:Suppress("MatchingDeclarationName")

package com.juancoob.kahootchallenge.buildsrc

object Libs {

    const val androidGradlePlugin = "com.android.tools.build:gradle:7.2.1"
    const val gradleVersionsBenManesPlugin = "com.github.ben-manes:gradle-versions-plugin:0.42.0"
    const val junit = "junit:junit:4.13.2"

    object Kotlin {
        private const val version = "1.7.0"
        const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"

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
}
