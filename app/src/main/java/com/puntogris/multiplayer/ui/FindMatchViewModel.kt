package com.puntogris.multiplayer.ui

import androidx.lifecycle.ViewModel
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.model.JoinedMatchInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class FindMatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    sharedPref: SharedPref
) : ViewModel() {

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching.asStateFlow()

    fun toggleQueueState() {
        _isSearching.value = !_isSearching.value
    }

    private val playerName = sharedPref.getPlayerName()

    fun startMatchmaking(): Flow<JoinedMatchInfo> {
        return flow {
            val data = matchRepository.getMatchFirestore(playerName)
            emitAll(data.map {
                val matchId = it?.id.toString()
                val full = it?.get("full").toString().toBoolean()
                val playerOne = it?.get("playerOne") as? HashMap<*, *>
                val playerPos = getPlayerPosition(playerOne?.get("name").toString())
                JoinedMatchInfo(matchId, full, playerPos)
            })
        }
    }

    suspend fun unsubscribeToMatchDatabase() =
        matchRepository.unsubscribeToMatchFirestore(playerName)

    private fun getPlayerPosition(player: String) =
        if (player == playerName) "playerOne" else "playerTwo"

}
