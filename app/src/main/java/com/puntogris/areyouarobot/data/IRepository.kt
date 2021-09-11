package com.puntogris.areyouarobot.data

import com.puntogris.areyouarobot.livedata.FirestoreQueryLiveData
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.SimpleResult

interface IRepository {
    suspend fun saveScoreFirestore(rankingEntry: RankingEntry): SimpleResult
    fun getRankingFirestore(): FirestoreQueryLiveData
}