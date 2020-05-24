package com.puntogris.areyouarobot.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.model.RankingEntry

internal object RankingEntryDeserializer: DocumentSnapshotDeserializer<RankingEntry>{
    override fun deserialize(input: DocumentSnapshot?): RankingEntry {
        val score = input?.get("score").toString().toInt()
        val playerName = input?.get("playerName").toString()
        return RankingEntry(score, playerName)
    }
}