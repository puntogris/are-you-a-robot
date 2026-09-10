package com.puntogris.areyouarobot.ui.compose

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.ui.game.GameViewModel
import com.puntogris.areyouarobot.ui.game.SaveRankingViewModel
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.launch

@Composable
internal fun PostGameScreen(
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
        modifier = modifier.padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            StatusChip(stringResource(R.string.result_eyebrow), Danger)
            Spacer(Modifier.height(20.dp))
            Text(stringResource(R.string.result_title), style = MaterialTheme.typography.headlineLarge)
            Spacer(Modifier.height(14.dp))
            Text(stringResource(R.string.result_body, time), style = MaterialTheme.typography.bodyLarge)
        }

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            MetricCard(
                stringResource(R.string.score_label),
                score.toString().padStart(2, '0'),
                Signal,
                Modifier.weight(1f)
            )
            MetricCard(
                stringResource(R.string.survival_label),
                "${time}s",
                Electric,
                Modifier.weight(1f)
            )
        }

        TerminalPanel(Modifier.fillMaxWidth(), Danger.copy(alpha = 0.45f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(Danger.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text("?", color = Danger, fontSize = 22.sp, fontWeight = FontWeight.Black)
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        stringResource(R.string.classification_label),
                        color = Muted,
                        style = MonoLabel.copy(fontSize = 10.sp)
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        stringResource(R.string.classification_value),
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }

        Column {
            PrimaryAction(
                stringResource(R.string.run_again),
                onPlayAgain,
                Modifier.fillMaxWidth()
            )
            SecondaryAction(
                stringResource(R.string.publish_score),
                { showSaveDialog = true },
                Modifier.fillMaxWidth()
            )
        }
    }

    if (showSaveDialog) {
        SaveScoreDialog(
            score,
            saveRankingViewModel,
            { showSaveDialog = false },
            onScoreSaved
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
        containerColor = Panel,
        shape = RoundedCornerShape(22.dp),
        icon = { StatusChip(stringResource(R.string.save_dialog_eyebrow), Electric) },
        title = {
            Text(
                stringResource(R.string.save_dialog_title),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    stringResource(R.string.save_dialog_body, score),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.height(18.dp))
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it.take(12) },
                    label = { Text(stringResource(R.string.dialog_nickname_hint)) },
                    enabled = !isSaving,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Electric,
                        unfocusedBorderColor = Stroke
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = !isSaving,
                colors = ButtonDefaults.textButtonColors(contentColor = Signal),
                onClick = {
                    scope.launch {
                        isSaving = true
                        val result = viewModel.savePlayerScore(score, playerName)
                        val message = when (result) {
                            SimpleResult.Success -> R.string.snack_save_score_success
                            SimpleResult.Failure -> R.string.snack_save_score_error
                        }
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        isSaving = false
                        onSaved()
                    }
                }
            ) {
                if (isSaving) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(stringResource(R.string.action_save), style = MonoLabel)
                }
            }
        },
        dismissButton = {
            TextButton(enabled = !isSaving, onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel), color = Muted, style = MonoLabel)
            }
        }
    )
}
