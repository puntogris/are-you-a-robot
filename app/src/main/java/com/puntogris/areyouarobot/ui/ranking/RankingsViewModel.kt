package com.puntogris.areyouarobot.ui.ranking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.areyouarobot.data.FirestoreQueryRankingEntryTransformation
import com.puntogris.areyouarobot.data.repo.Repository
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class RankingsViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val rankingUpdates: Flow<List<QueryItem<RankingEntry>>?> =
        FirestoreQueryRankingEntryTransformation
            .transform(repository.getRankingFirestore())

    val rankings: StateFlow<List<QueryItem<RankingEntry>>?> = rankingUpdates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
        initialValue = null
    )

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
