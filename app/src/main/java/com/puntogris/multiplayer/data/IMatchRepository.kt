package com.puntogris.multiplayer.data

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface IMatchRepository {
    suspend fun getMatchFirestore(playerName: String): Flow<DocumentSnapshot?>

    suspend fun unsubscribeToMatchFirestore(playerName: String): SimpleResult

    fun getMatchDataFirestore(matchId: String): Flow<DocumentSnapshot?>

    fun incrementScorePlayerFirestore(playerPos: String, matchId: String)
}
