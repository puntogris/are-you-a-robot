package com.puntogris.areyouarobot.ui.game

import androidx.lifecycle.ViewModel
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.data.repo.Repository
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.utils.SimpleResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SaveRankingViewModel @Inject constructor(
    private val repository: Repository,
    sharedPref: SharedPref
) : ViewModel() {

    val currentUsername = sharedPref.getPlayerName()

    suspend fun savePlayerScore(score: Int, playerName: String): SimpleResult {
        val rankingEntry = RankingEntry(score, playerName)
        return repository.saveScoreFirestore(rankingEntry)
    }
}