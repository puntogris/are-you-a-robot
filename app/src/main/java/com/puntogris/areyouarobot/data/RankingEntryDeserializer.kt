package com.puntogris.areyouarobot.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.model.RankingEntry

internal object RankingEntryDeserializer: DocumentSnapshotDeserializer<RankingEntry>{
    override fun deserialize(input: DocumentSnapshot?): RankingEntry {
        val score = input?.get("score").toString().toInt()
        val username = input?.get("username").toString()
        return RankingEntry(score, username)
    }
}