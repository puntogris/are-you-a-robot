package com.puntogris.multiplayer.ui

import androidx.lifecycle.*
import com.puntogris.multiplayer.data.MatchDeserializer
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.model.MatchModel
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
private const val INITIAL_INT_VALUE = 0
private const val MAX_PERCENTAGE = 100
private const val DEFAULT_LETTER_DIFFICULTY = 2
private var lettersDifficulty = DEFAULT_LETTER_DIFFICULTY

class MatchViewModel : ViewModel(){
    private val repo  = MatchRepository()

    private var _globalTime = MutableLiveData(INITIAL_INT_VALUE)
    val globalTime:LiveData<Int> = _globalTime

    private var _currentLetters = MutableLiveData<String>()
    val currentLetters:LiveData<String> = _currentLetters

    private var globalTimer:TimerTask? = null

    fun getMatchData(matchId:String): LiveData<MatchModel> {
        val data = repo.getMatchDataFirstore(matchId)
        return Transformations.map(data){
           MatchDeserializer.deserialize(it)
        }
    }
    fun initGame(){
        globalTimer = startTimer()

    }

    private fun startTimer(): TimerTask {
        return Timer().scheduleAtFixedRate(0,1000){
            val time = _globalTime.value!!.plus(1)
            _globalTime.postValue(time)}
    }

    private fun getLetters(){
        val allowedChars = ('a'..'z')
        _currentLetters.value = (1..lettersDifficulty)
            .map { allowedChars.random() }
            .joinToString("")
    }

    val letters = liveData{
        val allowedChars = ('a'..'z')
        val lettters = (1..lettersDifficulty)
            .map { allowedChars.random() }
            .joinToString("")
        emit(lettters)
    }



    fun endGame(){
        globalTimer?.cancel()
    }

//    private fun updateDifficulty(){
//        when(_score.value){
//            5 -> lettersDifficulty++
//            10 -> lettersDifficulty++
//            15 -> lettersDifficulty++
//        }
//    }
}