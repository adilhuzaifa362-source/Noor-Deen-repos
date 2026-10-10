package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.TajweedSpan
import com.example.model.TajweedType

@Composable
fun TajweedAnnotatedText(
    text: String,
    spans: List<TajweedSpan>,
    isTajweedEnabled: Boolean,
    modifier: Modifier = Modifier,
    fontSize: Int = 26,
    textAlign: TextAlign = TextAlign.Right,
    onTajweedRuleClick: ((TajweedType) -> Unit)? = null
) {
    val annotatedString = buildAnnotatedString {
        if (!isTajweedEnabled || spans.isEmpty()) {
            append(text)
        } else {
            val sortedSpans = spans.sortedBy { it.start }
            var lastIndex = 0

            for (span in sortedSpans) {
                val start = span.start.coerceIn(0, text.length)
                val end = span.end.coerceIn(start, text.length)

                if (start > lastIndex && lastIndex < text.length) {
                    append(text.substring(lastIndex, start))
                }

                if (start < end) {
                    withStyle(
                        style = SpanStyle(
                            color = span.type.color,
                            fontWeight = FontWeight.Bold,
                            background = span.type.color.copy(alpha = 0.12f)
                        )
                    ) {
                        append(text.substring(start, end))
                    }
                    lastIndex = end
                }
            }

            if (lastIndex < text.length) {
                append(text.substring(lastIndex))
            }
        }
    }

    Text(
        text = annotatedString,
        fontSize = fontSize.sp,
        lineHeight = (fontSize * 1.7).sp,
        fontFamily = FontFamily.Serif,
        fontWeight = FontWeight.Medium,
        textAlign = textAlign,
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun TajweedLegendBar(
    onRuleClick: (TajweedType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("tajweed_legend_bar"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tajweed Guide (قواعد التجويد)",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tap a rule to learn",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TajweedType.entries.forEach { rule ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onRuleClick(rule) }
                            .padding(4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(14.dp)
                                .clip(CircleShape)
                                .background(rule.color)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = rule.title,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TajweedLegendDialog(
    rule: TajweedType?,
    onDismiss: () -> Unit
) {
    if (rule == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(rule.color)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(text = rule.title, style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = rule.arabicTitle,
                        style = MaterialTheme.typography.labelLarge,
                        color = rule.color
                    )
                }
            }
        },
        text = {
            Column {
                Text(
                    text = rule.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Color Indicator:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(rule.color, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Highlighted in Uthmani verses",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got It")
            }
        }
    )
}
