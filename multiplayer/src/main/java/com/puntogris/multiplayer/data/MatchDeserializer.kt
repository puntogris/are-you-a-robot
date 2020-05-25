package com.puntogris.multiplayer.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.data.DocumentSnapshotDeserializer
import com.puntogris.multiplayer.model.MatchModel

internal object MatchDeserializer: DocumentSnapshotDeserializer<MatchModel> {

    override fun deserialize(input: DocumentSnapshot?): MatchModel {
        val playerOne = input?.get("playerOne") as HashMap<*,*>
        val playerTwo = input.get("playerTwo") as HashMap<*,*>
        val playerOneName = playerOne["name"].toString()
        val playerTwoName = playerTwo["name"].toString()
        val playerOneScore = playerOne["score"].toString().toInt()
        val playerTwoScore = playerTwo["score"].toString().toInt()

        return MatchModel(playerOneName,playerTwoName,playerOneScore,playerTwoScore)
    }
}