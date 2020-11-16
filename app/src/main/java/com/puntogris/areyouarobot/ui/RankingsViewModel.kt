package com.puntogris.areyouarobot.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.puntogris.areyouarobot.data.FirestoreQueryRankingEntryTransformation
import com.puntogris.areyouarobot.data.Repository
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

class RankingsViewModel @ViewModelInject constructor(private val repo:Repository) : ViewModel(){

    fun getRanking(): LiveData<List<QueryItem<RankingEntry>>> {
        val data = repo.getRankingFirestore()
        return FirestoreQueryRankingEntryTransformation.transform(data)
    }
}