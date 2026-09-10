package com.puntogris.multiplayer.ui

import android.os.CountDownTimer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.puntogris.multiplayer.data.MatchDeserializer
import com.puntogris.multiplayer.data.MatchRepository
import com.puntogris.areyouarobot.model.Match
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask
import kotlin.concurrent.scheduleAtFixedRate
import javax.inject.Inject

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val repo: MatchRepository
) : ViewModel() {

    private var timerJob: Job? = null
    private var matchDataJob: Job? = null
    private var globalTimer: TimerTask? = null

    private var matchId = ""
    private var playerPos = ""

    private val _currentLetters = MutableStateFlow("")
    val currentLetters: StateFlow<String> = _currentLetters.asStateFlow()

    private val _isTimeToGuess = MutableStateFlow(false)
    val isTimeToGuess: StateFlow<Boolean> = _isTimeToGuess.asStateFlow()

    private val _matchInfo = MutableStateFlow<Match?>(null)
    val matchInfo: StateFlow<Match?> = _matchInfo.asStateFlow()

    private val score = MutableStateFlow(INITIAL_INT_VALUE)

    private val _globalTime = MutableStateFlow(INITIAL_INT_VALUE)
    val globalTime: StateFlow<Int> = _globalTime.asStateFlow()

    private val _progressBarStatus = MutableStateFlow(INITIAL_INT_VALUE)
    val progressBarStatus: StateFlow<Int> = _progressBarStatus.asStateFlow()

    private val _gameEnded = MutableStateFlow(false)
    val gameEnded: StateFlow<Boolean> = _gameEnded.asStateFlow()

    private var timeDifficultyLetters = 1000L
    private var timeDifficultyGuess = 3000L
    private var lettersDifficulty = DEFAULT_LETTER_DIFFICULTY

    private val countDownTimer =
        object : CountDownTimer(timeDifficultyGuess, 10) {
            override fun onTick(millisUntilFinished: Long) {
                _progressBarStatus.value =
                    MAX_PERCENTAGE - (millisUntilFinished * MAX_PERCENTAGE / timeDifficultyGuess).toInt()
            }

            override fun onFinish() {
                gameOn()
            }
        }

    fun getMatchData(matchId: String) {
        matchDataJob?.cancel()
        matchDataJob = repo.getMatchDataFirestore(matchId)
            .filterNotNull()
            .map(MatchDeserializer::deserialize)
            .onEach { _matchInfo.value = it }
            .launchIn(viewModelScope)
    }

    fun initializeGame(matchId: String, playerPos: String) {
        _gameEnded.value = false
        this.matchId = matchId
        this.playerPos = playerPos
        globalTimer?.cancel()
        lettersDifficulty = DEFAULT_LETTER_DIFFICULTY
        score.value = INITIAL_INT_VALUE
        _globalTime.value = INITIAL_INT_VALUE
        globalTimer = startTimer()
        globalTimer?.run()
        gameOn()
    }

    private fun startTimer(): TimerTask {
        return Timer().scheduleAtFixedRate(0, 1000) {
            if (_globalTime.value >= MATCH_DURATION) _gameEnded.value = true
            else _globalTime.value += 1
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
        timerJob = viewModelScope.launch {
            delay(milliseconds)
            _isTimeToGuess.value = true
            countDownTimer.start()
        }
    }

    fun guessCorrect() {
        repo.incrementScorePlayerFirestore(playerPos, matchId)
        countDownTimer.cancel()
        timerJob?.cancel()
        score.value += 1
        gameOn()
    }

    fun gameEnded() {
        _progressBarStatus.value = MAX_PERCENTAGE
        globalTimer?.cancel()
        timerJob?.cancel()
        matchDataJob?.cancel()
        countDownTimer.cancel()
    }

    private fun updateDifficulty() {
        if (score.value == 5 && lettersDifficulty < 3) {
            lettersDifficulty++
        } else if (score.value == 10 && lettersDifficulty < 4) {
            lettersDifficulty++
        } else if (score.value == 15 && lettersDifficulty < 5) {
            lettersDifficulty++
        }
    }

    companion object {
        private const val MATCH_DURATION = 60
        private const val INITIAL_INT_VALUE = 0
        private const val MAX_PERCENTAGE = 100
        private const val DEFAULT_LETTER_DIFFICULTY = 2
    }

}
