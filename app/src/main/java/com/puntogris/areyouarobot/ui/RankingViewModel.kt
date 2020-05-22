package com.puntogris.areyouarobot.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.areyouarobot.data.FirestoreQueryRankingEntryTransformation
import com.puntogris.areyouarobot.data.Repository
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.herewego.diffcallback.QueryItem
import kotlinx.coroutines.launch

class RankingViewModel :ViewModel(){

    private val repo = Repository()

    fun getRanking():LiveData<List<QueryItem<RankingEntry>>>{
        val data = repo.getRankingFirestore()
        return FirestoreQueryRankingEntryTransformation.transform(data)
    }
}