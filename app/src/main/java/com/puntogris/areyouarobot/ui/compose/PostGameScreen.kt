package com.puntogris.areyouarobot.ui.compose

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.ui.game.GameViewModel

@Composable
internal fun PostGameScreen(
    modifier: Modifier,
    gameViewModel: GameViewModel,
    onPlayAgain: () -> Unit,
    onPublishScore: () -> Unit
) {
    val score by gameViewModel.score.observeAsState(0)
    val time by gameViewModel.globalTime.observeAsState(0)

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
                Brand,
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
                onPublishScore,
                Modifier.fillMaxWidth()
            )
        }
    }
}
