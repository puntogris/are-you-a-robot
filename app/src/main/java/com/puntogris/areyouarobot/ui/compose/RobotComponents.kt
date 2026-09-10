package com.puntogris.areyouarobot.ui.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke as DrawStroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.puntogris.areyouarobot.R

@Composable
internal fun GridBackdrop(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Ink)
    ) {
        Canvas(Modifier.fillMaxSize()) {
            val step = 36.dp.toPx()
            var x = 0f
            while (x <= size.width) {
                drawLine(Grid.copy(alpha = 0.18f), Offset(x, 0f), Offset(x, size.height), 1f)
                x += step
            }
            var y = 0f
            while (y <= size.height) {
                drawLine(Grid.copy(alpha = 0.18f), Offset(0f, y), Offset(size.width, y), 1f)
                y += step
            }
        }
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun AppTopBar(
    label: String,
    canClose: Boolean,
    showSettings: Boolean,
    onClose: () -> Unit,
    onSettings: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = label,
                color = if (canClose) Muted else Brand,
                style = MonoLabel,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            if (canClose) {
                IconButton(onClick = onClose) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.action_close),
                        tint = Paper
                    )
                }
            } else {
                Box(Modifier.size(48.dp), contentAlignment = Alignment.Center) {
                    BrandDot()
                }
            }
        },
        actions = {
            if (showSettings) {
                IconButton(onClick = onSettings) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = stringResource(R.string.settings_label),
                        tint = Paper
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Ink.copy(alpha = 0.96f),
            scrolledContainerColor = Ink
        )
    )
}

@Composable
internal fun AppBottomBar(
    homeSelected: Boolean,
    onHome: () -> Unit,
    onRankings: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Ink.copy(alpha = 0.96f))
            .navigationBarsPadding()
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Surface(
            color = Panel,
            shape = RoundedCornerShape(22.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Stroke),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                BottomDestination(
                    selected = homeSelected,
                    label = stringResource(R.string.nav_test),
                    onClick = onHome
                ) {
                    Icon(Icons.Default.Home, contentDescription = null)
                }
                BottomDestination(
                    selected = !homeSelected,
                    label = stringResource(R.string.rankings_label),
                    onClick = onRankings
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun RowScope.BottomDestination(
    selected: Boolean,
    label: String,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    Surface(
        onClick = onClick,
        color = if (selected) Brand.copy(alpha = 0.13f) else Color.Transparent,
        contentColor = if (selected) Brand else Muted,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .weight(1f)
            .height(52.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(Modifier.size(22.dp), contentAlignment = Alignment.Center) { icon() }
            Spacer(Modifier.width(9.dp))
            Text(text = label, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
internal fun StatusChip(text: String, color: Color) {
    Row(
        modifier = Modifier
            .border(1.dp, color.copy(alpha = 0.45f), RoundedCornerShape(50))
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(50))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(6.dp)
                .background(color, CircleShape)
        )
        Spacer(Modifier.width(7.dp))
        Text(text = text, color = color, style = MonoLabel.copy(fontSize = 10.sp))
    }
}

@Composable
internal fun BrandDot() {
    Box(
        modifier = Modifier
            .size(16.dp)
            .border(1.dp, Brand.copy(alpha = 0.45f), CircleShape)
            .padding(4.dp)
            .background(Brand, CircleShape)
    )
}

@Composable
internal fun Eyebrow(text: String, color: Color = Electric) {
    Text(text = text, color = color, style = MonoLabel)
}

@Composable
internal fun TerminalPanel(
    modifier: Modifier = Modifier,
    borderColor: Color = Stroke,
    padding: Dp = 20.dp,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = Panel.copy(alpha = 0.96f),
        shape = RoundedCornerShape(20.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
    ) {
        Box(Modifier.padding(padding)) { content() }
    }
}

@Composable
internal fun PrimaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(58.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Brand,
            contentColor = Ink,
            disabledContainerColor = Stroke,
            disabledContentColor = Muted
        )
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.width(12.dp))
        Text(text = "→", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
internal fun SecondaryAction(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier.height(54.dp),
        enabled = enabled,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = Electric)
    ) {
        Text(text = text, style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
internal fun MetricCard(
    label: String,
    value: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    TerminalPanel(modifier = modifier, padding = 16.dp) {
        Column {
            Text(text = label, color = Muted, style = MonoLabel.copy(fontSize = 10.sp))
            Spacer(Modifier.height(8.dp))
            Text(
                text = value,
                color = color,
                fontFamily = FontFamily.Monospace,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
internal fun ScannerMark(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val stroke = 2.dp.toPx()
        val center = this.center
        val radius = size.minDimension * 0.39f

        drawCircle(Grid.copy(alpha = 0.45f), radius = radius * 1.24f, style = DrawStroke(stroke))
        drawCircle(Brand.copy(alpha = 0.16f), radius = radius)
        drawArc(
            color = Brand,
            startAngle = -75f,
            sweepAngle = 230f,
            useCenter = false,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2f, radius * 2f),
            style = DrawStroke(stroke * 2f, cap = StrokeCap.Round)
        )
        drawLine(
            Electric.copy(alpha = 0.45f),
            Offset(center.x - radius * 1.25f, center.y),
            Offset(center.x + radius * 1.25f, center.y),
            stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 10f))
        )
        drawLine(
            Electric.copy(alpha = 0.45f),
            Offset(center.x, center.y - radius * 1.25f),
            Offset(center.x, center.y + radius * 1.25f),
            stroke,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 10f))
        )

        val faceWidth = radius * 0.88f
        val faceHeight = radius * 0.62f
        drawRoundRect(
            color = PanelRaised,
            topLeft = Offset(center.x - faceWidth / 2, center.y - faceHeight / 2),
            size = Size(faceWidth, faceHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(14f, 14f),
            style = DrawStroke(stroke * 1.5f)
        )
        drawCircle(Brand, radius = stroke * 2.2f, center = Offset(center.x - faceWidth * 0.2f, center.y - 4f))
        drawCircle(Brand, radius = stroke * 2.2f, center = Offset(center.x + faceWidth * 0.2f, center.y - 4f))
        drawLine(
            Electric,
            Offset(center.x - faceWidth * 0.2f, center.y + faceHeight * 0.2f),
            Offset(center.x + faceWidth * 0.2f, center.y + faceHeight * 0.2f),
            stroke * 1.5f,
            StrokeCap.Round
        )
    }
}

@Composable
internal fun EmptyMessage(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StatusChip(text = "404", color = Warning)
        Spacer(Modifier.height(16.dp))
        Text(
            text = text,
            color = Muted,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
