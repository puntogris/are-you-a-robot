package com.puntogris.areyouarobot.data

import com.puntogris.areyouarobot.livedata.FirestoreQueryLiveData
import com.puntogris.areyouarobot.model.RankingEntry

interface IRepository {
    fun saveScoreFirestore(rankingEntry: RankingEntry)
    fun getRankingFirestore(): FirestoreQueryLiveData
}