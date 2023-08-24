package com.puntogris.areyouarobot.ui.ranking

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.puntogris.areyouarobot.data.FirestoreQueryRankingEntryTransformation
import com.puntogris.areyouarobot.data.repo.Repository
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    fun getRankings(): LiveData<List<QueryItem<RankingEntry>>> {
        val data = repository.getRankingFirestore()
        return FirestoreQueryRankingEntryTransformation.transform(data)
    }
}