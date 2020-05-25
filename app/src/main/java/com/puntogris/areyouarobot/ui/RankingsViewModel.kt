package com.puntogris.areyouarobot.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.puntogris.areyouarobot.data.FirestoreQueryRankingEntryTransformation
import com.puntogris.areyouarobot.data.Repository
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry

class RankingsViewModel : ViewModel(){

    private val repo = Repository()

    fun getRanking(): LiveData<List<QueryItem<RankingEntry>>> {
        val data = repo.getRankingFirestore()
        return FirestoreQueryRankingEntryTransformation.transform(data)
    }
}