package com.puntogris.areyouarobot.ui.compose

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.ui.game.GameViewModel
import com.puntogris.areyouarobot.ui.game.SaveRankingViewModel
import com.puntogris.areyouarobot.utils.SimpleResult
import kotlinx.coroutines.launch

@Composable
internal fun SaveScoreScreen(
    modifier: Modifier,
    gameViewModel: GameViewModel,
    saveRankingViewModel: SaveRankingViewModel,
    onSaved: () -> Unit,
    onCancel: () -> Unit
) {
    val score by gameViewModel.score.observeAsState(0)
    var playerName by rememberSaveable { mutableStateOf(saveRankingViewModel.currentUsername) }
    var isSaving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column {
            Eyebrow(stringResource(R.string.save_ranking_eyebrow))
            Spacer(Modifier.height(12.dp))
            Text(
                stringResource(R.string.save_ranking_title),
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.height(14.dp))
            Text(
                stringResource(R.string.save_ranking_body, score),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        MetricCard(
            label = stringResource(R.string.score_label),
            value = score.toString().padStart(2, '0'),
            color = Brand,
            modifier = Modifier.fillMaxWidth()
        )

        TerminalPanel(Modifier.fillMaxWidth()) {
            Column {
                Text(
                    stringResource(R.string.ranking_identity_label),
                    color = Muted,
                    style = MonoLabel.copy(fontSize = 10.sp)
                )
                Spacer(Modifier.height(14.dp))
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it.take(12) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.ranking_nickname_hint)) },
                    supportingText = {
                        Text(
                            "${playerName.length}/12",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    },
                    enabled = !isSaving,
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = terminalTextFieldColors(),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        autoCorrectEnabled = false,
                        imeAction = ImeAction.Done
                    )
                )
                Spacer(Modifier.height(18.dp))
                PrimaryAction(
                    text = stringResource(R.string.publish_score_action),
                    onClick = {
                        scope.launch {
                            isSaving = true
                            when (saveRankingViewModel.savePlayerScore(score, playerName.trim())) {
                                SimpleResult.Success -> {
                                    Toast.makeText(
                                        context,
                                        R.string.snack_save_score_success,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onSaved()
                                }

                                SimpleResult.Failure -> {
                                    Toast.makeText(
                                        context,
                                        R.string.snack_save_score_error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    isSaving = false
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = playerName.isNotBlank(),
                    loading = isSaving
                )
            }
        }

        SecondaryAction(
            text = stringResource(R.string.action_cancel),
            onClick = onCancel,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isSaving
        )
    }
}
