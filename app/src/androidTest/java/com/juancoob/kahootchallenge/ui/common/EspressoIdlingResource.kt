package com.juancoob.kahootchallenge.ui.common

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    val idlingResource = CountingIdlingResource("countingIdlingResource")

    init {
        idlingResource.dumpStateToLogs()
    }

    fun increment() = idlingResource.increment()

    fun decrement() {
        if (!idlingResource.isIdleNow) {
            idlingResource.decrement()
        }
    }
}

inline fun <T> wrapBlockWithIdlingResources(block: () -> T): T {
    EspressoIdlingResource.increment()
    return try {
        block()
    } finally {
        EspressoIdlingResource.decrement()
    }
}
