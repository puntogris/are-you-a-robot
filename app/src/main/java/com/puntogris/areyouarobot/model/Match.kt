package com.puntogris.areyouarobot.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Match(
    val playerOneName: String,
    val playerTwoName: String,
    val playerOneScore: Int,
    val playerTwoScore: Int
) : Parcelable {

    fun result(): Winner {
        return when {
            playerOneScore > playerTwoScore -> Winner.PlayerOne
            playerTwoScore > playerOneScore -> Winner.PlayerTwo
            else -> Winner.Draw
        }
    }

    enum class Winner {
        PlayerOne, PlayerTwo, Draw
    }
}