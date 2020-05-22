package com.puntogris.areyouarobot.utils

object Utils {

    fun createDefaultRandomName():String{
        var name = "robot"
        repeat(4) {
            name += (0..9).random().toString()
        }
        return name
    }
}