package com.puntogris.areyouarobot.ui.compose

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
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
import com.puntogris.areyouarobot.ui.game.GameViewModel

@Composable
fun GameScreen(
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GameMetricCard(
                    label = stringResource(R.string.score_label),
                    value = score.toString().padStart(2, '0'),
                    color = Brand,
                    modifier = Modifier.weight(1f)
                )
                GameMetricCard(
                    label = stringResource(R.string.time_label),
                    value = "${time}s",
                    color = Electric,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(Modifier.height(8.dp))
            Spacer(Modifier.weight(1f))

            TerminalPanel(
                modifier = Modifier.fillMaxWidth(),
                padding = 16.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    StatusChip(
                        text = stringResource(if (isGuessing) R.string.game_input_phase else R.string.game_memory_phase),
                        color = if (isGuessing) Electric else Brand
                    )
                    Spacer(Modifier.height(14.dp))
                    Text(
                        text = stringResource(if (isGuessing) R.string.game_repeat else R.string.game_memorize),
                        color = Muted,
                        style = MonoLabel,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(10.dp))

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
                                fontSize = 46.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 6.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    Spacer(Modifier.height(2.dp))
                }
            }

            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(8.dp))

            if (isGuessing) {
                ResponseWindow(
                    progress = progress,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Text(
                    text = stringResource(R.string.game_memory_hint),
                    color = Muted,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun GameMetricCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    TerminalPanel(modifier = modifier, padding = 11.dp) {
        Column {
            Text(text = label, color = Muted, style = MonoLabel.copy(fontSize = 10.sp))
            Spacer(Modifier.height(5.dp))
            Text(
                text = value,
                color = color,
                fontFamily = GoogleSansFlex,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold
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

    TerminalPanel(
        modifier = modifier,
        borderColor = color.copy(alpha = 0.3f),
        padding = 10.dp,
        shape = RoundedCornerShape(14.dp)
    ) {
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
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { timeLeft },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(5.dp)
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
private fun GameScreenPreview() {
    RobotScreenPreview(Screen.Game) { modifier ->
        GameScreenContent(
            modifier = modifier,
            letters = "RY7",
            score = 7,
            time = 23,
            progress = 38,
            isGuessing = false,
            guess = "3",
            onGuessChange = {},
            focusRequester = remember { FocusRequester() }
        )
    }
}
