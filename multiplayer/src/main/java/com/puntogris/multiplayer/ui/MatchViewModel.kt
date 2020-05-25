package com.puntogris.multiplayer.ui

import android.os.CountDownTimer
import androidx.lifecycle.*
import com.puntogris.multiplayer.data.MatchDeserializer
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.multiplayer.model.MatchModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate

class MatchViewModel : ViewModel(){

    private val repo = MatchRepository()
    private var timerJob : Job? = null
    private var globalTimer: TimerTask? = null

    private var matchId = ""
    private var playerPos = ""

    private var _currentLetters = MutableLiveData<String>()
    val currentLetters: LiveData<String> = _currentLetters

    private var _isTimeToGuess = MutableLiveData<Boolean>()
    val isTimeToGuess: LiveData<Boolean> = _isTimeToGuess

    private var _matchInfo = MutableLiveData<MatchModel>()
    val matchInfo: LiveData<MatchModel> = _matchInfo

    private var _score = MutableLiveData(INITIAL_INT_VALUE)
    val score: LiveData<Int> = _score

    private var _globalTime = MutableLiveData(INITIAL_INT_VALUE)
    val globalTime: LiveData<Int> = _globalTime

    private var _progressBarStatus = MutableLiveData<Int>()
    val progressBarStatus: LiveData<Int> = _progressBarStatus

    private var _gamEnded = MutableLiveData(false)
    val gameEnded: LiveData<Boolean> = _gamEnded

    private var timeDifficultyLetters = 1000L
    private var timeDifficultyGuess = 3000L
    private var lettersDifficulty = DEFAULT_LETTER_DIFFICULTY


    private val countDownTimer =
        object : CountDownTimer(timeDifficultyGuess, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _progressBarStatus.value = MAX_PERCENTAGE - (millisUntilFinished * MAX_PERCENTAGE / timeDifficultyGuess).toInt()
            }
            override fun onFinish() {
                gameOn()
            }
        }


    fun getMatchData(matchId:String): LiveData<MatchModel> {
        val data = repo.getMatchDataFirstore(matchId)
        return Transformations.map(data){
            val match = MatchDeserializer.deserialize(it)
            _matchInfo.postValue(match)
            match
        }
    }

    fun initializeGame(matchId: String, playerPos:String){
        _gamEnded.value = false
        this.matchId = matchId
        this.playerPos = playerPos
        globalTimer?.cancel()
        lettersDifficulty = DEFAULT_LETTER_DIFFICULTY
        _score.value = INITIAL_INT_VALUE
        _globalTime.value = INITIAL_INT_VALUE
        globalTimer = startTimer()
        globalTimer?.run()
        gameOn()
    }

    private fun startTimer(): TimerTask {
        return Timer().scheduleAtFixedRate(0,1000){
            val time = _globalTime.value!!.plus(1)
            if (time >= 10){
                _gamEnded.postValue(true)
            }else{
                _globalTime.postValue(time)
            }
        }
    }

    private fun getLetters(){
        val allowedChars = ('a'..'z')
        _currentLetters.value = (1..lettersDifficulty)
            .map { allowedChars.random() }
            .joinToString("")
    }

    private fun gameOn() {
        countDownTimer.cancel()
        updateDifficulty()
        getLetters()
        _isTimeToGuess.value = false
        _progressBarStatus.value = INITIAL_INT_VALUE
        startTimerShowLetters(timeDifficultyLetters)
    }

    private fun startTimerShowLetters(milliseconds:Long){
        viewModelScope.launch {
            timerJob = Job()
            delay(milliseconds)
            _isTimeToGuess.value = true
            countDownTimer.start()
        }
    }

    fun guessCorrect(){
        repo.incrementScorePlayerFirestore(playerPos,matchId)
        countDownTimer.cancel()
        timerJob?.cancel()
        val score = _score.value!!.plus(1)
        _score.value = score
        gameOn()
    }

    fun gameEnded(){
        _progressBarStatus.value = MAX_PERCENTAGE
        globalTimer?.cancel()
        timerJob?.cancel()
        countDownTimer.cancel()
    }

    private fun updateDifficulty(){
        if (score.value!! == 5 && lettersDifficulty <3){lettersDifficulty++}
        else if (score.value!! == 10 && lettersDifficulty <4){lettersDifficulty++}
        else if (score.value!! == 15 && lettersDifficulty <5){lettersDifficulty++}
    }

    companion object{
        private const val INITIAL_INT_VALUE = 0
        private const val MAX_PERCENTAGE = 100
        private const val DEFAULT_LETTER_DIFFICULTY = 2
    }

}