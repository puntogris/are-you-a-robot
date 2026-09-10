package com.puntogris.areyouarobot.data.repo

import com.google.firebase.firestore.DocumentSnapshot
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.flow.Flow

interface IRepository {
    suspend fun saveScoreFirestore(rankingEntry: RankingEntry): SimpleResult

    fun getRankingFirestore(): Flow<List<DocumentSnapshot>>
}
