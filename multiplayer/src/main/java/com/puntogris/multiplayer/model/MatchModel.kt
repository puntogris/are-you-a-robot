package com.puntogris.multiplayer.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MatchModel (
    val playerOneName:String,
    val playerTwoName:String,
    val playerOneScore:Int,
    val playerTwoScore:Int
):Parcelable{
    fun playerOneWon() = playerOneScore > playerTwoScore

    fun playerTwoWon() = playerTwoScore > playerTwoScore

    fun draw() = playerOneScore == playerTwoScore

}