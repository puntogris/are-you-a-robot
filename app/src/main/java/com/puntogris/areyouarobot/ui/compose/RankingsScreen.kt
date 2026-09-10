package com.puntogris.areyouarobot.ui.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.ui.ranking.RankingsViewModel

@Composable
internal fun RankingsScreen(modifier: Modifier, viewModel: RankingsViewModel) {
    val source = remember(viewModel) { viewModel.getRankings() }
    val rankings by source.observeAsState()
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
            rankings.orEmpty().isEmpty() -> EmptyMessage(stringResource(R.string.ranking_empty))
            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                itemsIndexed(rankings.orEmpty()) { index, entry ->
                    val accent = when (index) {
                        0 -> Warning
                        1 -> Electric
                        2 -> Signal
                        else -> Stroke
                    }
                    TerminalPanel(Modifier.fillMaxWidth(), accent.copy(alpha = 0.7f), 14.dp) {
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
                            Text(entry.item.playerName, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Text(entry.item.score.toString(), color = Signal, style = MonoValue)
                        }
                    }
                }
                item { Spacer(Modifier.height(14.dp)) }
            }
        }
    }
}
