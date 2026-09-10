package com.puntogris.areyouarobot.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.model.RankingEntry
import com.puntogris.areyouarobot.ui.ranking.RankingsViewModel

@Composable
internal fun RankingsScreen(modifier: Modifier, viewModel: RankingsViewModel) {
    val rankings by viewModel.rankings.collectAsStateWithLifecycle()

    RankingsContent(
        modifier = modifier,
        rankings = rankings?.map { it.item }
    )
}

@Composable
private fun RankingsContent(
    modifier: Modifier,
    rankings: List<RankingEntry>?
) {
    Column(modifier.padding(horizontal = 20.dp)) {
        Spacer(Modifier.height(14.dp))
        Eyebrow(stringResource(R.string.ranking_eyebrow))
        Spacer(Modifier.height(10.dp))
        Text(stringResource(R.string.ranking_title), style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text(stringResource(R.string.ranking_body), style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(22.dp))
        when {
            rankings == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Electric)
            }
            rankings.isEmpty() -> EmptyMessage(stringResource(R.string.ranking_empty))
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(rankings) { index, entry ->
                    val accent = if (index == 0) Brand else Muted
                    TerminalPanel(Modifier.fillMaxWidth(), padding = 14.dp) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = accent.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(42.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text((index + 1).toString().padStart(2, '0'), color = accent, style = MonoValue)
                                }
                            }
                            Spacer(Modifier.width(14.dp))
                            Text(entry.playerName, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(entry.score.toString(), color = Brand, style = MonoValue)
                        }
                    }
                }
                item { Spacer(Modifier.height(14.dp)) }
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun RankingsScreenPreview() {
    RobotScreenPreview(Screen.Rankings) { modifier ->
        RankingsContent(
            modifier = modifier,
            rankings = listOf(
                RankingEntry(score = 18, playerName = "NEXUS"),
                RankingEntry(score = 14, playerName = "BYTE"),
                RankingEntry(score = 11, playerName = "ADA")
            )
        )
    }
}
