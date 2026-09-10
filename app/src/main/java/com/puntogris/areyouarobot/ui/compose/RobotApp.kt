package com.puntogris.areyouarobot.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
private fun HomeScreen(
    modifier: Modifier,
    onPlay: () -> Unit
) {
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Eyebrow(stringResource(R.string.home_eyebrow))
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.home_title),
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.fillMaxWidth(0.9f)
            )
            Spacer(Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.home_body),
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.fillMaxWidth(0.92f)
            )
        }

        TerminalPanel(
            modifier = Modifier.fillMaxWidth(),
            padding = 8.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(235.dp),
                contentAlignment = Alignment.Center
            ) {
                ScannerMark(Modifier.size(185.dp))
            }
        }

        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("MEM_01", color = Muted, style = MonoLabel.copy(fontSize = 10.sp))
                Text("4.0 SEC", color = Muted, style = MonoLabel.copy(fontSize = 10.sp))
                Text("NO AUTOFILL", color = Muted, style = MonoLabel.copy(fontSize = 10.sp))
            }
            Spacer(Modifier.height(14.dp))
            PrimaryAction(
                text = stringResource(R.string.begin_test),
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun GameScreen(
    modifier: Modifier,
    viewModel: GameViewModel,
    onGameOver: () -> Unit
) {
    val letters by viewModel.currentLetters.collectAsStateWithLifecycle()
    val score by viewModel.score.collectAsStateWithLifecycle()
    val time by viewModel.globalTime.collectAsStateWithLifecycle()
    val progress by viewModel.progressBarStatus.collectAsStateWithLifecycle()
    val isGuessing by viewModel.isTimeToGuess.collectAsStateWithLifecycle()
    val didLose by viewModel.didPlayerLose.collectAsStateWithLifecycle()
    var guess by remember { mutableStateOf("") }
    var keyboardWasOpened by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    DisposableEffect(viewModel) {
        viewModel.initializeGame()
        onDispose { viewModel.stopGame() }
    }

    LaunchedEffect(isGuessing) {
        if (isGuessing) {
            guess = ""
            keyboardWasOpened = true
            focusRequester.requestFocus()
            keyboard?.show()
        } else if (!keyboardWasOpened) {
            keyboard?.hide()
        }
    }

    LaunchedEffect(didLose) {
        if (didLose) {
            keyboard?.hide()
            viewModel.playerLost()
            onGameOver()
        }
    }

    GameScreenContent(
        modifier = modifier,
        letters = letters,
        score = score,
        time = time,
        progress = progress,
        isGuessing = isGuessing,
        guess = guess,
        onGuessChange = { value ->
            if (isGuessing) {
                guess = value
                if (value.equals(letters, ignoreCase = true)) viewModel.playerWon()
            }
        },
        focusRequester = focusRequester
    )
}

@Composable
private fun GameScreenContent(
    modifier: Modifier,
    letters: String,
    score: Int,
    time: Int,
    progress: Int,
    isGuessing: Boolean,
    guess: String,
    onGuessChange: (String) -> Unit,
    focusRequester: FocusRequester
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .padding(horizontal = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricCard(
                label = stringResource(R.string.score_label),
                value = score.toString().padStart(2, '0'),
                color = Brand,
                modifier = Modifier.weight(1f)
            )
            MetricCard(
                label = stringResource(R.string.time_label),
                value = "${time}s",
                color = Electric,
                modifier = Modifier.weight(1f)
            )
        }

        TerminalPanel(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            padding = 24.dp
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatusChip(
                    text = stringResource(if (isGuessing) R.string.game_input_phase else R.string.game_memory_phase),
                    color = if (isGuessing) Electric else Brand
                )
                Spacer(Modifier.height(24.dp))
                Text(
                    text = stringResource(if (isGuessing) R.string.game_repeat else R.string.game_memorize),
                    color = Muted,
                    style = MonoLabel,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(18.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    SignalInput(
                        value = guess,
                        onValueChange = onGuessChange,
                        focusRequester = focusRequester,
                        modifier = Modifier.alpha(if (isGuessing) 1f else 0f)
                    )
                    if (!isGuessing) {
                        Text(
                            text = letters.uppercase(),
                            color = Paper,
                            fontFamily = GoogleSansFlex,
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 8.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
        }

        if (isGuessing) {
            ResponseWindow(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 14.dp)
            )
        } else {
            Text(
                text = stringResource(R.string.game_memory_hint),
                color = Muted,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
            )
        }
    }
}

@Composable
private fun SignalInput(
    value: String,
    onValueChange: (String) -> Unit,
    focusRequester: FocusRequester,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.uppercase()) },
        modifier = modifier
            .fillMaxWidth()
            .focusRequester(focusRequester),
        placeholder = {
            Text(
                stringResource(R.string.game_input_hint),
                color = Muted,
                fontFamily = GoogleSansFlex,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            color = Paper,
            fontFamily = GoogleSansFlex,
            textAlign = TextAlign.Center,
            letterSpacing = 5.sp
        ),
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = terminalTextFieldColors(),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrectEnabled = false,
            imeAction = ImeAction.Done
        )
    )
}

@Composable
private fun ResponseWindow(
    progress: Int,
    modifier: Modifier = Modifier
) {
    val timeLeft = 1f - progress.coerceIn(0, 100) / 100f
    val color = if (timeLeft < 0.28f) Danger else Warning

    TerminalPanel(modifier = modifier, borderColor = color.copy(alpha = 0.55f), padding = 14.dp) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.response_window),
                    color = color,
                    style = MonoLabel.copy(fontSize = 10.sp)
                )
                Text(
                    "${(timeLeft * 100).toInt()}%",
                    color = color,
                    style = MonoLabel.copy(fontSize = 10.sp)
                )
            }
            Spacer(Modifier.height(9.dp))
            LinearProgressIndicator(
                progress = { timeLeft },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(50)),
                color = color,
                trackColor = Stroke.copy(alpha = 0.55f)
            )
        }
    }
}

@Composable
internal fun RobotScreenPreview(
    screen: Screen,
    content: @Composable (Modifier) -> Unit
) {
    RobotTheme {
        AppFrame(screen = screen, onNavigate = {}, content = content)
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    RobotScreenPreview(Screen.Home) { modifier ->
        HomeScreen(modifier = modifier, onPlay = {})
    }
}

@PreviewLightDark
@Composable
private fun GameScreenPreview() {
    RobotScreenPreview(Screen.Game) { modifier ->
        GameScreenContent(
            modifier = modifier,
            letters = "RY7",
            score = 7,
            time = 23,
            progress = 38,
            isGuessing = false,
            guess = "",
            onGuessChange = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}
