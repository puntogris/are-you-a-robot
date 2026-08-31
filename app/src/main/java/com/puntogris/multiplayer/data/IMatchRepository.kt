package com.puntogris.multiplayer.data

import com.puntogris.areyouarobot.utils.SimpleResult
import com.puntogris.multiplayer.livedata.FirestoreDocumentLiveData

interface IMatchRepository {
    suspend fun getMatchFirestore(playerName: String): FirestoreDocumentLiveData

    suspend fun unsubscribeToMatchFirestore(playerName: String): SimpleResult

    fun getMatchDataFirestore(matchId: String): FirestoreDocumentLiveData

    fun incrementScorePlayerFirestore(playerPos: String, matchId: String)
}