package com.puntogris.areyouarobot.ui.game

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {

    private var timerJob: Job? = null
    private var globalTimer: TimerTask? = null

    private val _currentLetters = MutableStateFlow("")
    val currentLetters: StateFlow<String> = _currentLetters.asStateFlow()

    private val _isTimeToGuess = MutableStateFlow(false)
    val isTimeToGuess: StateFlow<Boolean> = _isTimeToGuess.asStateFlow()

    private val _didPlayerLose = MutableStateFlow(false)
    val didPlayerLose: StateFlow<Boolean> = _didPlayerLose.asStateFlow()

    private val _score = MutableStateFlow(INITIAL_INT_VALUE)
    val score: StateFlow<Int> = _score.asStateFlow()

    private val _globalTime = MutableStateFlow(INITIAL_INT_VALUE)
    val globalTime: StateFlow<Int> = _globalTime.asStateFlow()

    private val _progressBarStatus = MutableStateFlow(INITIAL_INT_VALUE)
    val progressBarStatus: StateFlow<Int> = _progressBarStatus.asStateFlow()

    private var timeDifficultyLetters = 3000L
    private var timeDifficultyGuess = 4000L
    private var lettersDifficulty = DEFAULT_LETTER_DIFFICULTY

    private val countDownTimer =
        object : CountDownTimer(timeDifficultyGuess, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _progressBarStatus.value =
                    MAX_PERCENTAGE - (millisUntilFinished * MAX_PERCENTAGE / timeDifficultyGuess).toInt()
            }

            override fun onFinish() {
                _didPlayerLose.value = true
            }
        }

    fun initializeGame() {
        globalTimer?.cancel()
        globalTimer = startTimer()
        lettersDifficulty = DEFAULT_LETTER_DIFFICULTY
        _score.value = INITIAL_INT_VALUE
        _globalTime.value = INITIAL_INT_VALUE
        gameOn()
    }

    private fun startTimer(): TimerTask {
        return Timer().scheduleAtFixedRate(0, 1000) {
            _globalTime.value += 1
        }
    }

    private fun getLetters() {
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

    private fun startTimerShowLetters(milliseconds: Long) {
        _didPlayerLose.value = false
        timerJob = viewModelScope.launch {
            delay(milliseconds.milliseconds)
            _isTimeToGuess.value = true
            countDownTimer.start()
        }
    }

    fun playerWon() {
        countDownTimer.cancel()
        timerJob?.cancel()
        _score.value += 1
        gameOn()
    }

    fun playerLost() {
        _didPlayerLose.value = false
        _progressBarStatus.value = MAX_PERCENTAGE
        stopGame()
    }

    fun stopGame() {
        countDownTimer.cancel()
        globalTimer?.cancel()
        timerJob?.cancel()
    }

    override fun onCleared() {
        stopGame()
        super.onCleared()
    }

    private fun updateDifficulty() {
        when (_score.value) {
            5 -> lettersDifficulty++
            10 -> lettersDifficulty++
            15 -> lettersDifficulty++
        }
    }

    companion object {
        private const val INITIAL_INT_VALUE = 0
        private const val MAX_PERCENTAGE = 100
        private const val DEFAULT_LETTER_DIFFICULTY = 2
    }
}
