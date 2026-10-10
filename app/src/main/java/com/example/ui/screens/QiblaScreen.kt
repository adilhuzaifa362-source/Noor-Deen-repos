package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NearMe
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.NoorDeenViewModel
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QiblaScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val qiblaBearing by viewModel.qiblaBearing.collectAsState()
    val deviceHeading by viewModel.deviceHeading.collectAsState()
    val isFacingQibla by viewModel.isFacingQibla.collectAsState()
    val selectedCity by viewModel.selectedCity.collectAsState()
    val sensorAccuracy by viewModel.sensorAccuracy.collectAsState()

    DisposableEffect(Unit) {
        viewModel.startCompass()
        onDispose {
            viewModel.stopCompass()
        }
    }

    val animatedHeading by animateFloatAsState(
        targetValue = deviceHeading,
        animationSpec = tween(durationMillis = 200),
        label = "compass_rotation"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("qibla_screen_view"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("qibla_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Hardware Qibla Compass",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Calculated from ${selectedCity.name} (Great-Circle Geodesic)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Status Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isFacingQibla)
                    MaterialTheme.colorScheme.secondaryContainer
                else
                    MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isFacingQibla) Icons.Default.CheckCircle else Icons.Default.NearMe,
                        contentDescription = "Status",
                        tint = if (isFacingQibla) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isFacingQibla) "Facing Holy Kaaba (Makkah)!" else "Rotate Device to Align",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = if (isFacingQibla) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Kaaba Angle: ${qiblaBearing.toInt()}° • Device: ${deviceHeading.toInt()}°",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Compass Dial Canvas
        Box(
            modifier = Modifier
                .size(280.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .testTag("compass_dial_canvas"),
            contentAlignment = Alignment.Center
        ) {
            val primaryColor = MaterialTheme.colorScheme.primary
            val goldColor = MaterialTheme.colorScheme.secondary
            val outlineColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)

            Canvas(modifier = Modifier.size(280.dp)) {
                val center = Offset(size.width / 2, size.height / 2)
                val radius = size.width / 2 - 16.dp.toPx()

                // Outer Ring
                drawCircle(
                    color = outlineColor,
                    radius = radius,
                    center = center,
                    style = Stroke(width = 3.dp.toPx())
                )

                // Dial rotation according to device heading
                rotate(degrees = -animatedHeading, pivot = center) {
                    // Draw tick marks
                    for (i in 0 until 360 step 15) {
                        val angleRad = Math.toRadians(i.toDouble())
                        val isMajor = i % 90 == 0
                        val tickLength = if (isMajor) 18.dp.toPx() else 8.dp.toPx()
                        val startX = center.x + (radius - tickLength) * sin(angleRad).toFloat()
                        val startY = center.y - (radius - tickLength) * cos(angleRad).toFloat()
                        val endX = center.x + radius * sin(angleRad).toFloat()
                        val endY = center.y - radius * cos(angleRad).toFloat()

                        drawLine(
                            color = if (i == 0) Color.Red else outlineColor,
                            start = Offset(startX, startY),
                            end = Offset(endX, endY),
                            strokeWidth = if (isMajor) 3.dp.toPx() else 1.5.dp.toPx()
                        )
                    }

                    // Qibla Indicator Line & Kaaba Emblem
                    val qiblaRad = Math.toRadians(qiblaBearing)
                    val qiblaX = center.x + (radius - 28.dp.toPx()) * sin(qiblaRad).toFloat()
                    val qiblaY = center.y - (radius - 28.dp.toPx()) * cos(qiblaRad).toFloat()

                    // Line to Qibla
                    drawLine(
                        color = goldColor,
                        start = center,
                        end = Offset(qiblaX, qiblaY),
                        strokeWidth = 4.dp.toPx()
                    )

                    // Kaaba Gold Dot
                    drawCircle(
                        color = goldColor,
                        radius = 12.dp.toPx(),
                        center = Offset(qiblaX, qiblaY)
                    )
                }

                // Fixed Center Target Arrow (Pointing Up)
                val pointerPath = Path().apply {
                    moveTo(center.x, center.y - radius + 24.dp.toPx())
                    lineTo(center.x - 14.dp.toPx(), center.y - 10.dp.toPx())
                    lineTo(center.x + 14.dp.toPx(), center.y - 10.dp.toPx())
                    close()
                }
                drawPath(
                    path = pointerPath,
                    color = if (isFacingQibla) goldColor else primaryColor
                )

                // Center Pivot Dot
                drawCircle(
                    color = primaryColor,
                    radius = 8.dp.toPx(),
                    center = center
                )
            }

            // Center Calligraphy / Degree Badge
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${deviceHeading.toInt()}°",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "الكعبة",
                    fontFamily = FontFamily.Serif,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Calibration Note
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.padding(horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Calibration Tip",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sensor Status: ${if (sensorAccuracy >= 2) "Calibrated (High)" else "Wave phone in figure-8 motion to calibrate"}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
