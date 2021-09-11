package com.puntogris.multiplayer.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.model.JoinedMatchInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class FindMatchViewModel @Inject constructor(
    private val matchRepository: MatchRepository
): ViewModel(){

    private var playerName:String = ""

    fun setPlayerName(name:String){
        playerName = name
    }

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

    fun unsubscribeToMatch(){
        viewModelScope.launch {
            try {
                matchRepository.unsubscribeToMatchFirestore(playerName)
            }catch (e: Exception){
                //manage error
            }
        }
    }

    private fun getPlayerPosition(player:String):String{
        return if (player == playerName){
            "playerOne"
        }else "playerTwo"
    }

}

