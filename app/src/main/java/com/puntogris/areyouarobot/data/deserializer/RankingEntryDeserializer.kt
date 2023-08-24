package com.puntogris.areyouarobot.data.deserializer

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.Constants.PLAYER_NAME_FIELD
import com.puntogris.areyouarobot.utils.Constants.SCORE_FIELD

object RankingEntryDeserializer : DocumentSnapshotDeserializer<RankingEntry> {

    override fun deserialize(input: DocumentSnapshot?): RankingEntry {
        val score = input?.get(SCORE_FIELD).toString().toIntOrNull() ?: 0
        val playerName = input?.get(PLAYER_NAME_FIELD).toString()
        return RankingEntry(score, playerName)
    }
}