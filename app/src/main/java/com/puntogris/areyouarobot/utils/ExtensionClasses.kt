package com.puntogris.areyouarobot.utils

sealed class SimpleResult{
    object Success: SimpleResult()
    object Failure: SimpleResult()
}
