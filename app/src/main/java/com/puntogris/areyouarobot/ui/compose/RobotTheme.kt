package com.puntogris.areyouarobot.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val Ink = Color(0xFF070B10)
internal val Panel = Color(0xFF0D151E)
internal val PanelRaised = Color(0xFF121F2B)
internal val Grid = Color(0xFF1A3440)
internal val Signal = Color(0xFF66F5A5)
internal val Electric = Color(0xFF3BD9FF)
internal val Warning = Color(0xFFFFB45A)
internal val Danger = Color(0xFFFF607D)
internal val Paper = Color(0xFFF2F7F8)
internal val Muted = Color(0xFF8A9BA6)
internal val Stroke = Color(0xFF29404C)

internal val MonoLabel = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    letterSpacing = 1.4.sp
)

internal val MonoValue = TextStyle(
    fontFamily = FontFamily.Monospace,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp
)

@Composable
internal fun RobotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Signal,
            onPrimary = Ink,
            secondary = Electric,
            onSecondary = Ink,
            background = Ink,
            onBackground = Paper,
            surface = Panel,
            onSurface = Paper,
            surfaceVariant = PanelRaised,
            onSurfaceVariant = Muted,
            outline = Stroke,
            error = Danger
        ),
        typography = MaterialTheme.typography.copy(
            headlineLarge = TextStyle(
                color = Paper,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
                lineHeight = 44.sp,
                letterSpacing = (-1.2).sp
            ),
            headlineMedium = TextStyle(
                color = Paper,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                lineHeight = 35.sp,
                letterSpacing = (-0.7).sp
            ),
            titleLarge = TextStyle(
                color = Paper,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            bodyLarge = TextStyle(
                color = Muted,
                fontSize = 17.sp,
                lineHeight = 25.sp
            ),
            bodyMedium = TextStyle(
                color = Muted,
                fontSize = 14.sp,
                lineHeight = 21.sp
            ),
            labelLarge = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 0.8.sp
            )
        ),
        content = content
    )
}
