package com.puntogris.areyouarobot.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref
import com.puntogris.areyouarobot.diffcallback.QueryItem
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.ui.game.GameViewModel
import com.puntogris.areyouarobot.ui.game.SaveRankingViewModel
import com.puntogris.areyouarobot.ui.ranking.RankingsViewModel
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.launch

private val Background = Color(0xFF3D3845)
private val Primary = Color(0xFF84BD97)
private val Accent = Color(0xFFE88B2E)

private enum class Screen {
    Home,
    Rankings,
    Game,
    PostGame,
    Settings
}

@Composable
fun RobotApp(
    gameViewModel: GameViewModel,
    rankingsViewModel: RankingsViewModel,
    saveRankingViewModel: SaveRankingViewModel,
    sharedPref: SharedPref
) {
    val colors = androidx.compose.material3.darkColorScheme(
        primary = Primary,
        secondary = Accent,
        background = Background,
        surface = Background
    )

    MaterialTheme(colorScheme = colors) {
        var screen by rememberSaveable { mutableStateOf(Screen.Home) }

        BackHandler(enabled = screen != Screen.Home) {
            screen = Screen.Home
        }

        RobotScaffold(
            screen = screen,
            onNavigate = { screen = it }
        ) { modifier ->
            when (screen) {
                Screen.Home -> WelcomeScreen(
                    modifier = modifier,
                    onPlay = { screen = Screen.Game }
                )

                Screen.Rankings -> RankingsScreen(
                    modifier = modifier,
                    viewModel = rankingsViewModel
                )

                Screen.Game -> GameScreen(
                    modifier = modifier,
                    viewModel = gameViewModel,
                    onGameOver = { screen = Screen.PostGame }
                )

                Screen.PostGame -> PostGameScreen(
                    modifier = modifier,
                    gameViewModel = gameViewModel,
                    saveRankingViewModel = saveRankingViewModel,
                    onPlayAgain = { screen = Screen.Game },
                    onScoreSaved = { screen = Screen.Home }
                )

                Screen.Settings -> SettingsScreen(
                    modifier = modifier,
                    sharedPref = sharedPref,
                    onSaved = { screen = Screen.Home }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RobotScaffold(
    screen: Screen,
    onNavigate: (Screen) -> Unit,
    content: @Composable (Modifier) -> Unit
) {
    val showBottomBar = screen == Screen.Home || screen == Screen.Rankings || screen == Screen.PostGame
    val showTopBar = screen == Screen.Home || screen == Screen.Game || screen == Screen.Settings

    Scaffold(
        containerColor = Background,
        topBar = {
            if (showTopBar) {
                TopAppBar(
                    title = { },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
                    navigationIcon = {
                        if (screen == Screen.Game || screen == Screen.Settings) {
                            IconButton(onClick = { onNavigate(Screen.Home) }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = stringResource(R.string.action_close)
                                )
                            }
                        }
                    },
                    actions = {
                        if (screen == Screen.Home) {
                            IconButton(onClick = { onNavigate(Screen.Settings) }) {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = stringResource(R.string.settings_label)
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Background) {
                    NavigationBarItem(
                        selected = screen == Screen.Home,
                        onClick = { onNavigate(Screen.Home) },
                        icon = { Icon(Icons.Default.Home, contentDescription = null) },
                        label = { Text(stringResource(R.string.menu_label)) }
                    )
                    NavigationBarItem(
                        selected = screen == Screen.Rankings,
                        onClick = { onNavigate(Screen.Rankings) },
                        icon = { Icon(Icons.Default.Star, contentDescription = null) },
                        label = { Text(stringResource(R.string.rankings_label)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        content(Modifier.padding(innerPadding))
    }
}

@Composable
private fun WelcomeScreen(
    modifier: Modifier,
    onPlay: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.app_name),
                color = Primary,
                fontSize = 34.sp
            )
            Spacer(Modifier.width(12.dp))
            Image(
                painter = painterResource(R.drawable.ic_group_15),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.size(55.dp)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(R.drawable.ic_technology),
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.message_incentive_to_play),
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }

        AccentButton(
            text = stringResource(R.string.prove_it),
            onClick = onPlay,
            modifier = Modifier.width(200.dp)
        )
    }
}

@Composable
private fun GameScreen(
    modifier: Modifier,
    viewModel: GameViewModel,
    onGameOver: () -> Unit
) {
    val letters by viewModel.currentLetters.observeAsState("")
    val score by viewModel.score.observeAsState(0)
    val time by viewModel.globalTime.observeAsState(0)
    val progress by viewModel.progressBarStatus.observeAsState(0)
    val isGuessing by viewModel.isTimeToGuess.observeAsState(false)
    val didLose by viewModel.didPlayerLose.observeAsState(false)
    var guess by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    DisposableEffect(viewModel) {
        viewModel.initializeGame()
        onDispose { viewModel.stopGame() }
    }

    LaunchedEffect(isGuessing) {
        if (isGuessing) {
            guess = ""
            focusRequester.requestFocus()
            keyboard?.show()
        } else {
            keyboard?.hide()
        }
    }

    LaunchedEffect(didLose) {
        if (didLose) {
            viewModel.playerLost()
            onGameOver()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "${stringResource(R.string.score)} $score", color = Color.White, fontSize = 19.sp)
            Text(text = "${stringResource(R.string.timer)} $time", color = Color.White, fontSize = 19.sp)
        }

        if (isGuessing) {
            OutlinedTextField(
                value = guess,
                onValueChange = { value ->
                    guess = value
                    if (value == letters) viewModel.playerWon()
                },
                modifier = Modifier
                    .width(180.dp)
                    .align(Alignment.Center)
                    .focusRequester(focusRequester),
                label = { Text(stringResource(R.string.code)) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    imeAction = ImeAction.Done
                )
            )
        } else {
            Text(
                text = letters,
                modifier = Modifier.align(Alignment.Center),
                color = Primary,
                fontSize = 40.sp
            )
        }

        if (isGuessing) {
            LinearProgressIndicator(
                progress = { progress.coerceIn(0, 100) / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .align(Alignment.BottomCenter),
                color = Accent
            )
        }
    }
}

@Composable
private fun PostGameScreen(
    modifier: Modifier,
    gameViewModel: GameViewModel,
    saveRankingViewModel: SaveRankingViewModel,
    onPlayAgain: () -> Unit,
    onScoreSaved: () -> Unit
) {
    val score by gameViewModel.score.observeAsState(0)
    val time by gameViewModel.globalTime.observeAsState(0)
    var showSaveDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.you_lost), color = Primary, fontSize = 28.sp)
            Spacer(Modifier.height(24.dp))
            Text(stringResource(R.string.try_again_robot), color = Color.White, fontSize = 20.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${stringResource(R.string.score)} $score", color = Color.White, fontSize = 19.sp)
            Spacer(Modifier.height(16.dp))
            Text("${stringResource(R.string.timer)} $time", color = Color.White, fontSize = 19.sp)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AccentButton(text = stringResource(R.string.try_again), onClick = onPlayAgain)
            TextButton(onClick = { showSaveDialog = true }) {
                Text(stringResource(R.string.save_score))
            }
        }
    }

    if (showSaveDialog) {
        SaveScoreDialog(
            score = score,
            viewModel = saveRankingViewModel,
            onDismiss = { showSaveDialog = false },
            onSaved = onScoreSaved
        )
    }
}

@Composable
private fun SaveScoreDialog(
    score: Int,
    viewModel: SaveRankingViewModel,
    onDismiss: () -> Unit,
    onSaved: () -> Unit
) {
    var playerName by rememberSaveable { mutableStateOf(viewModel.currentUsername) }
    var isSaving by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = { if (!isSaving) onDismiss() },
        title = { Text(stringResource(R.string.save_score)) },
        text = {
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it.take(12) },
                label = { Text(stringResource(R.string.dialog_nickname_hint)) },
                enabled = !isSaving,
                singleLine = true
            )
        },
        confirmButton = {
            TextButton(
                enabled = !isSaving,
                onClick = {
                    scope.launch {
                        isSaving = true
                        val result = viewModel.savePlayerScore(score, playerName)
                        val message = when (result) {
                            SimpleResult.Success -> R.string.snack_save_score_success
                            SimpleResult.Failure -> R.string.snack_save_score_error
                        }
                        android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_SHORT).show()
                        isSaving = false
                        onSaved()
                    }
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.action_save))
                }
            }
        },
        dismissButton = {
            TextButton(enabled = !isSaving, onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        }
    )
}

@Composable
private fun RankingsScreen(
    modifier: Modifier,
    viewModel: RankingsViewModel
) {
    val rankingsSource = remember(viewModel) { viewModel.getRankings() }
    val rankings by rankingsSource.observeAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(32.dp))
        Text(stringResource(R.string.rankings_label), color = Primary, fontSize = 30.sp)
        Spacer(Modifier.height(16.dp))

        if (rankings == null) {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
        } else {
            RankingList(rankings.orEmpty())
        }
    }
}

@Composable
private fun RankingList(rankings: List<QueryItem<RankingEntry>>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(
            items = rankings,
            key = { index, item -> "${item.id}-$index" }
        ) { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("${index + 1}", color = Primary, modifier = Modifier.width(44.dp))
                Text(
                    text = item.item.playerName,
                    color = Color.White,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = item.item.score.toString(),
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SettingsScreen(
    modifier: Modifier,
    sharedPref: SharedPref,
    onSaved: () -> Unit
) {
    var playerName by rememberSaveable { mutableStateOf(sharedPref.getPlayerName()) }
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(stringResource(R.string.settings_label), color = Primary, fontSize = 30.sp)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedTextField(
                value = playerName,
                onValueChange = { playerName = it.take(12) },
                label = { Text(stringResource(R.string.nickname)) },
                singleLine = true
            )
            Spacer(Modifier.height(24.dp))
            AccentButton(
                text = stringResource(R.string.action_save),
                onClick = {
                    sharedPref.setPlayerName(playerName)
                    onSaved()
                }
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            TextButton(onClick = {
                runCatching { uriHandler.openUri("https://robot.puntogris.com/privacy-policy.html") }
            }) {
                Text(stringResource(R.string.privacy_policy))
            }
            TextButton(onClick = {
                runCatching { uriHandler.openUri("https://robot.puntogris.com/terms-and-conditions.html") }
            }) {
                Text(stringResource(R.string.terms_and_conditions))
            }
        }
    }
}

@Composable
private fun AccentButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Accent,
            contentColor = Color.White
        )
    ) {
        Text(text)
    }
}
