package com.example.implab.tasks

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

fun main() = runBlocking {
    val coroutineScope = CoroutineScope(Dispatchers.Default)
    val sharedFlow = flow {
        emit(5)
        emit(7)
        emit(9)
    }.shareIn(coroutineScope, SharingStarted.Lazily)

    launch {
        sharedFlow.collect {
            println("I'm converted Flow: $it")
        }
    }

    val stateFlow = MutableStateFlow("")

    launch {
        stateFlow.collect {
            println(it)
        }
    }

    stateFlow.value = "1"
    stateFlow.value = "2"
    stateFlow.value = "3"
    stateFlow.value = "4"
    stateFlow.value = "5"
    stateFlow.value = "6"

    delay(100)
}