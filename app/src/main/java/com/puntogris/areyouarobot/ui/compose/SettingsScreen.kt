package com.puntogris.areyouarobot.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R
import com.puntogris.areyouarobot.SharedPref

@Composable
internal fun SettingsScreen(modifier: Modifier, sharedPref: SharedPref, onSaved: () -> Unit) {
    var playerName by rememberSaveable { mutableStateOf(sharedPref.getPlayerName()) }
    val uriHandler = LocalUriHandler.current
    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Eyebrow(stringResource(R.string.settings_eyebrow))
            Spacer(Modifier.height(10.dp))
            Text(stringResource(R.string.settings_title), style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(8.dp))
            Text(stringResource(R.string.settings_body), style = MaterialTheme.typography.bodyMedium)
        }
        TerminalPanel(Modifier.fillMaxWidth(), Electric.copy(alpha = 0.45f)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .border(1.dp, Electric, CircleShape)
                        .background(Electric.copy(alpha = 0.1f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, null, tint = Electric, modifier = Modifier.size(34.dp))
                }
                Spacer(Modifier.height(20.dp))
                OutlinedTextField(
                    value = playerName,
                    onValueChange = { playerName = it.take(12) },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.nickname)) },
                    supportingText = {
                        Text("${playerName.length}/12", Modifier.fillMaxWidth(), textAlign = TextAlign.End)
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Electric,
                        unfocusedBorderColor = Stroke,
                        cursorColor = Electric
                    )
                )
                Spacer(Modifier.height(14.dp))
                PrimaryAction(
                    stringResource(R.string.save_identity),
                    {
                        sharedPref.setPlayerName(playerName)
                        onSaved()
                    },
                    Modifier.fillMaxWidth()
                )
            }
        }
        TerminalPanel(Modifier.fillMaxWidth(), padding = 10.dp) {
            Column {
                Text(
                    stringResource(R.string.legal_label),
                    color = Muted,
                    style = MonoLabel.copy(fontSize = 10.sp),
                    modifier = Modifier.padding(start = 10.dp)
                )
                SecondaryAction(
                    stringResource(R.string.privacy_policy),
                    { runCatching { uriHandler.openUri("https://robot.puntogris.com/privacy-policy.html") } },
                    Modifier.fillMaxWidth()
                )
                SecondaryAction(
                    stringResource(R.string.terms_and_conditions),
                    { runCatching { uriHandler.openUri("https://robot.puntogris.com/terms-and-conditions.html") } },
                    Modifier.fillMaxWidth()
                )
            }
        }
    }
}
