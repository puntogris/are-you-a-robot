package com.puntogris.areyouarobot.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R

@Composable
fun HomeScreen(
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
                style = MaterialTheme.typography.headlineMedium,
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

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    RobotScreenPreview(Screen.Home) { modifier ->
        HomeScreen(modifier = modifier, onPlay = {})
    }
}
