package com.puntogris.areyouarobot.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.ui.game.GameViewModel
import com.puntogris.areyouarobot.ui.game.SaveRankingViewModel
import com.puntogris.areyouarobot.ui.ranking.RankingsViewModel

internal enum class Screen {
    Home,
    Rankings,
    Game,
    PostGame,
    SaveScore,
    Settings
}

@Composable
fun RobotApp(
    gameViewModel: GameViewModel,
    rankingsViewModel: RankingsViewModel,
    saveRankingViewModel: SaveRankingViewModel,
    sharedPref: SharedPref
) {
    RobotTheme {
        var screen by rememberSaveable { mutableStateOf(Screen.Home) }

        BackHandler(enabled = screen != Screen.Home) {
            screen = if (screen == Screen.SaveScore) Screen.PostGame else Screen.Home
        }

        AppFrame(screen = screen, onNavigate = { screen = it }) { modifier ->
            when (screen) {
                Screen.Home -> HomeScreen(modifier) { screen = Screen.Game }
                Screen.Rankings -> RankingsScreen(modifier, rankingsViewModel)
                Screen.Game -> GameScreen(modifier, gameViewModel) { screen = Screen.PostGame }
                Screen.PostGame -> PostGameScreen(
                    modifier = modifier,
                    gameViewModel = gameViewModel,
                    onPlayAgain = { screen = Screen.Game },
                    onPublishScore = { screen = Screen.SaveScore }
                )

                Screen.SaveScore -> SaveScoreScreen(
                    modifier = modifier,
                    gameViewModel = gameViewModel,
                    saveRankingViewModel = saveRankingViewModel,
                    onSaved = { screen = Screen.Rankings },
                    onCancel = { screen = Screen.PostGame }
                )

                Screen.Settings -> SettingsScreen(modifier, sharedPref) { screen = Screen.Home }
            }
        }
    }
}

@Composable
internal fun AppFrame(
    screen: Screen,
    onNavigate: (Screen) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val topLabel = when (screen) {
        Screen.Home -> stringResource(R.string.protocol_label)
        Screen.Rankings -> stringResource(R.string.ranking_terminal_label)
        Screen.Game -> stringResource(R.string.game_terminal_label)
        Screen.PostGame -> stringResource(R.string.result_terminal_label)
        Screen.SaveScore -> stringResource(R.string.save_score_terminal_label)
        Screen.Settings -> stringResource(R.string.settings_terminal_label)
    }
    val showBottomBar = screen == Screen.Home || screen == Screen.Rankings

    Scaffold(
        containerColor = Ink,
        topBar = {
            AppTopBar(
                label = topLabel,
                canClose = !showBottomBar,
                showSettings = screen == Screen.Home,
                onClose = {
                    onNavigate(if (screen == Screen.SaveScore) Screen.PostGame else Screen.Home)
                },
                onSettings = { onNavigate(Screen.Settings) }
            )
        },
        bottomBar = {
            if (showBottomBar) {
                AppBottomBar(
                    homeSelected = screen == Screen.Home,
                    onHome = { onNavigate(Screen.Home) },
                    onRankings = { onNavigate(Screen.Rankings) }
                )
            }
        }
    ) { innerPadding ->
        GridBackdrop(Modifier.padding(innerPadding)) {
            content(Modifier.fillMaxSize())
        }
    }
}
