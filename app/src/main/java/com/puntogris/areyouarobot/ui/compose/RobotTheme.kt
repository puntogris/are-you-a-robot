package com.puntogris.areyouarobot.ui.compose

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R

internal val Ink = Color(0xFF070B10)
internal val Panel = Color(0xFF0D151E)
internal val PanelRaised = Color(0xFF121F2B)
internal val Grid = Color(0xFF1A3440)
internal val Brand = Color(0xFFFF9D23)
internal val Signal = Color(0xFF66F5A5)
internal val Electric = Color(0xFF3BD9FF)
internal val Warning = Color(0xFFFFD166)
internal val Danger = Color(0xFFFF607D)
internal val Paper = Color(0xFFF2F7F8)
internal val Muted = Color(0xFF8A9BA6)
internal val Stroke = Color(0xFF29404C)

private val GoogleFontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

internal val GoogleSansFlex = FontFamily(
    Font(
        googleFont = GoogleFont("Google Sans Flex"),
        fontProvider = GoogleFontsProvider,
        weight = FontWeight.Normal
    ),
    Font(
        googleFont = GoogleFont("Google Sans Flex"),
        fontProvider = GoogleFontsProvider,
        weight = FontWeight.Bold
    ),
    Font(
        googleFont = GoogleFont("Google Sans Flex"),
        fontProvider = GoogleFontsProvider,
        weight = FontWeight.Black
    )
)

internal val MonoLabel = TextStyle(
    fontFamily = GoogleSansFlex,
    fontWeight = FontWeight.Bold,
    fontSize = 12.sp,
    letterSpacing = 1.4.sp
)

internal val MonoValue = TextStyle(
    fontFamily = GoogleSansFlex,
    fontWeight = FontWeight.Bold,
    fontSize = 16.sp
)

@Composable
internal fun RobotTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Brand,
            onPrimary = Ink,
            primaryContainer = PanelRaised,
            onPrimaryContainer = Brand,
            inversePrimary = Brand,
            secondary = Electric,
            onSecondary = Ink,
            secondaryContainer = PanelRaised,
            onSecondaryContainer = Electric,
            tertiary = Signal,
            onTertiary = Ink,
            tertiaryContainer = PanelRaised,
            onTertiaryContainer = Signal,
            background = Ink,
            onBackground = Paper,
            surface = Panel,
            onSurface = Paper,
            surfaceVariant = PanelRaised,
            onSurfaceVariant = Muted,
            surfaceTint = Brand,
            inverseSurface = Paper,
            inverseOnSurface = Ink,
            outline = Stroke,
            outlineVariant = Grid,
            error = Danger,
            onError = Ink,
            errorContainer = PanelRaised,
            onErrorContainer = Danger,
            scrim = Ink,
            surfaceDim = Ink,
            surfaceBright = PanelRaised,
            surfaceContainerLowest = Ink,
            surfaceContainerLow = Panel,
            surfaceContainer = Panel,
            surfaceContainerHigh = PanelRaised,
            surfaceContainerHighest = PanelRaised
        ),
        typography = MaterialTheme.typography.copy(
            headlineLarge = TextStyle(
                color = Paper,
                fontFamily = GoogleSansFlex,
                fontWeight = FontWeight.Black,
                fontSize = 42.sp,
                lineHeight = 44.sp,
                letterSpacing = (-1.2).sp
            ),
            headlineMedium = TextStyle(
                color = Paper,
                fontFamily = GoogleSansFlex,
                fontWeight = FontWeight.Black,
                fontSize = 32.sp,
                lineHeight = 35.sp,
                letterSpacing = (-0.7).sp
            ),
            titleLarge = TextStyle(
                color = Paper,
                fontFamily = GoogleSansFlex,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp
            ),
            bodyLarge = TextStyle(
                color = Muted,
                fontFamily = GoogleSansFlex,
                fontSize = 17.sp,
                lineHeight = 25.sp
            ),
            bodyMedium = TextStyle(
                color = Muted,
                fontFamily = GoogleSansFlex,
                fontSize = 14.sp,
                lineHeight = 21.sp
            ),
            labelLarge = TextStyle(
                fontFamily = GoogleSansFlex,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                letterSpacing = 0.8.sp
            )
        ),
        content = content
    )
}
