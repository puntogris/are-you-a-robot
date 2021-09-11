package com.puntogris.multiplayer.data

import com.puntogris.multiplayer.livedata.FirestoreDocumentLiveData

interface IMatchRepository {
    suspend fun getMatchFirestore(playerName:String): FirestoreDocumentLiveData
    suspend fun unsubscribeToMatchFirestore(playerName: String)
    fun getMatchDataFirestore(matchId:String):FirestoreDocumentLiveData
    fun incrementScorePlayerFirestore(playerPos: String,matchId: String)
}