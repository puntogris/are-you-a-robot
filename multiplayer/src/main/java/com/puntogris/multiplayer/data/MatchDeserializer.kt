package com.puntogris.multiplayer.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.data.deserializer.DocumentSnapshotDeserializer
import com.puntogris.areyouarobot.model.Match

internal object MatchDeserializer: DocumentSnapshotDeserializer<Match> {

    override fun deserialize(input: DocumentSnapshot?): Match {
        val playerOne = input?.get("playerOne") as HashMap<*,*>
        val playerTwo = input.get("playerTwo") as HashMap<*,*>
        val playerOneName = playerOne["name"].toString()
        val playerTwoName = playerTwo["name"].toString()
        val playerOneScore = playerOne["score"].toString().toInt()
        val playerTwoScore = playerTwo["score"].toString().toInt()

        return Match(playerOneName, playerTwoName, playerOneScore, playerTwoScore)
    }
}