package com.puntogris.areyouarobot.ui.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.ui.compose.RobotApp
import com.puntogris.areyouarobot.ui.game.GameViewModel
import com.puntogris.areyouarobot.ui.game.SaveRankingViewModel
import com.puntogris.areyouarobot.ui.ranking.RankingsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val gameViewModel: GameViewModel by viewModels()
    private val rankingsViewModel: RankingsViewModel by viewModels()
    private val saveRankingViewModel: SaveRankingViewModel by viewModels()

    @Inject
    lateinit var sharedPref: SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RobotApp(
                gameViewModel = gameViewModel,
                rankingsViewModel = rankingsViewModel,
                saveRankingViewModel = saveRankingViewModel,
                sharedPref = sharedPref
            )
        }
    }
}
