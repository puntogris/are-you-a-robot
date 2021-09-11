package com.puntogris.areyouarobot.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.scheduleAtFixedRate

@HiltViewModel
class GameViewModel @Inject constructor(): ViewModel(){

    private var timerJob :Job? = null
    private var globalTimer:TimerTask? = null

    private var _currentLetters = MutableLiveData<String>()
    val currentLetters:LiveData<String> = _currentLetters

    private var _isTimeToGuess = MutableLiveData<Boolean>()
    val isTimeToGuess:LiveData<Boolean> = _isTimeToGuess

    //true = on, false = off
    private var _didPlayerLose = MutableLiveData<Boolean>()
    val didPlayerLose:LiveData<Boolean> = _didPlayerLose

    private var _score = MutableLiveData(INITIAL_INT_VALUE)
    val score:LiveData<Int> = _score

    private var _globalTime = MutableLiveData(INITIAL_INT_VALUE)
    val globalTime:LiveData<Int> = _globalTime

    private var _progressBarStatus = MutableLiveData<Int>()
    val progressBarStatus:LiveData<Int> = _progressBarStatus

    private var timeDifficultyLetters = 3000L
    private var timeDifficultyGuess = 4000L
    private var lettersDifficulty = DEFAULT_LETTER_DIFFICULTY


    private val countDownTimer =
        object : CountDownTimer(timeDifficultyGuess, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _progressBarStatus.value = MAX_PERCENTAGE - (millisUntilFinished * MAX_PERCENTAGE / timeDifficultyGuess).toInt()
            }
            override fun onFinish() {
                _didPlayerLose.postValue(true)
            }
        }

    fun initializeGame(){
        globalTimer?.cancel()
        globalTimer = startTimer()
        globalTimer?.run()
        lettersDifficulty = DEFAULT_LETTER_DIFFICULTY
        _score.value = INITIAL_INT_VALUE
        _globalTime.value = INITIAL_INT_VALUE
        gameOn()
    }

    private fun startTimer():TimerTask{
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

     private fun gameOn() {
         countDownTimer.cancel()
         updateDifficulty()
         getLetters()
         _isTimeToGuess.value = false
         _progressBarStatus.value = INITIAL_INT_VALUE
         startTimerShowLetters(timeDifficultyLetters)
    }

    private fun startTimerShowLetters(milliseconds:Long){
        _didPlayerLose.value = false
        viewModelScope.launch {
            timerJob = Job()
            delay(milliseconds)
            _isTimeToGuess.value = true
            countDownTimer.start()
        }
    }

    fun playerWon(){
        countDownTimer.cancel()
        timerJob?.cancel()
        val score = _score.value!!.plus(1)
        _score.value = score
        gameOn()
    }

    fun playerLost(){
        _progressBarStatus.value = MAX_PERCENTAGE
        globalTimer?.cancel()
        timerJob?.cancel()
    }

    private fun updateDifficulty(){
        when(_score.value){
            5 -> lettersDifficulty++
            10 -> lettersDifficulty++
            15 -> lettersDifficulty++
        }
    }
    companion object{
        private const val INITIAL_INT_VALUE = 0
        private const val MAX_PERCENTAGE = 100
        private const val DEFAULT_LETTER_DIFFICULTY = 2
    }
}