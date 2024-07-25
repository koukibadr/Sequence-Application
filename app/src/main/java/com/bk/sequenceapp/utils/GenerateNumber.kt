package com.bk.sequenceapp.utils

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.random.Random
import kotlin.random.nextInt


class GenerateNumber() {

    var seconds: Int = 0

    init {
        seconds = Random.nextInt(5..10)
    }

    suspend fun generateNumber() : Int {
        return coroutineScope {
            delayForSeconds(seconds*1000L)
            return@coroutineScope Random.nextInt(1..3)
        }
    }

    private suspend fun delayForSeconds(seconds: Long) {
        delay(seconds)
    }
}