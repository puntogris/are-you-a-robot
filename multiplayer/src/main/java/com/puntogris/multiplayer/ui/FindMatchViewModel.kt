package com.puntogris.multiplayer.ui

import androidx.lifecycle.*
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.livedata.FirestoreDocumentLiveData
import com.puntogris.multiplayer.model.MatchModel
import com.puntogris.multiplayer.model.MatchRoom
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.collect
import java.lang.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class FindMatchViewModel:ViewModel(){
    private val repo = MatchRepository()
    private var playerName:String = ""

    fun setPlayerName(name:String){
        playerName = name
    }

     suspend fun startMatchmaking(): LiveData<MatchRoom>{
        val data = repo.getMatchFirestore(playerName)
        return Transformations.map(data){
            val matchId = it?.id.toString()
            val full = it?.get("full").toString().toBoolean()
            MatchRoom(matchId,full)
        }
    }

    fun unsubscribeToMatch(){
        viewModelScope.launch {
            try {
                repo.unsubscribeToMatchFirestore(playerName)
            }catch (e:Exception){
                //manage error
            }
        }
    }

}

