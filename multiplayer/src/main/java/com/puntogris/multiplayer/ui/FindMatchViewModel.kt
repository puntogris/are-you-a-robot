package com.puntogris.multiplayer.ui

import androidx.lifecycle.*
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.model.JoinedMatchInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FindMatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository,
    sharedPref: SharedPref
): ViewModel(){

    private val _isSearching = MutableLiveData(false)
    val isSearching: LiveData<Boolean> = _isSearching

    fun toggleQueueState(){
        _isSearching.value = !_isSearching.value!!
    }

    private val playerName = sharedPref.getPlayerName()

    suspend fun startMatchmaking(): LiveData<JoinedMatchInfo> {
        val data = matchRepository.getMatchFirestore(playerName)
        return Transformations.map(data){
            val matchId = it?.id.toString()
            val full = it?.get("full").toString().toBoolean()
            val playerOne = it?.get("playerOne") as? HashMap<*, *>
            val playerPos = getPlayerPosition(playerOne?.get("name").toString())
            JoinedMatchInfo(matchId, full, playerPos)
        }
    }

    suspend fun unsubscribeToMatchDatabase() = matchRepository.unsubscribeToMatchFirestore(playerName)

    private fun getPlayerPosition(player: String) =
        if (player == playerName) "playerOne" else "playerTwo"

}

