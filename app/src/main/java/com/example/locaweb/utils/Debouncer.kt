package com.example.locaweb.utils

import kotlinx.coroutines.*

class Debouncer(private val intervalMillis: Long) {
    private var debounceJob: Job? = null

    fun debounce(action: suspend () -> Unit) {
        debounceJob?.cancel()
        debounceJob = CoroutineScope(Dispatchers.Default).launch {
            delay(intervalMillis)
            action()
        }
    }
}