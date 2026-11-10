package com.saiful.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonBright
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.SurfaceContainerDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.TextMuted
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun RatingDialog(
    currentRating: Float?,
    onConfirm: (Float) -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedScore by remember {
        mutableFloatStateOf(currentRating ?: 7.0f)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = SurfaceContainerLowestDark,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = NeonAmber,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = if (currentRating != null) "Edit Your Rating" else "Rate This Title",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Large Badge with rating value
                val badgeShape = RoundedCornerShape(16.dp)
                Box(
                    modifier = Modifier
                        .clip(badgeShape)
                        .background(SurfaceContainerDark)
                        .border(1.dp, NeonAmber.copy(alpha = 0.5f), badgeShape)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = NeonAmber,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = String.format(Locale.US, "%.1f", selectedScore),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonAmber
                        )
                        Text(
                            text = "/ 10",
                            fontSize = 14.sp,
                            color = TextMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Slider from 0.5 to 10.0 (step: 0.5)
                Slider(
                    value = selectedScore,
                    onValueChange = { rawValue ->
                        // Snap to nearest 0.5
                        selectedScore = ((rawValue * 2).roundToInt() / 2f).coerceIn(0.5f, 10.0f)
                    },
                    valueRange = 0.5f..10.0f,
                    steps = 18,
                    colors = SliderDefaults.colors(
                        thumbColor = NeonAmber,
                        activeTrackColor = NeonAmber,
                        inactiveTrackColor = SurfaceContainerDark
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Slide to adjust your score (0.5 – 10.0)",
                    fontSize = 12.sp,
                    color = TextMuted,
                    textAlign = TextAlign.Center
                )

                if (currentRating != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = onDelete,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = CrimsonBright
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            CrimsonBright.copy(alpha = 0.6f)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Text(
                            text = "Remove Rating",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(selectedScore) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = CinemaCrimson,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Submit Rating",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = TextMuted,
                    fontSize = 14.sp
                )
            }
        },
        modifier = modifier
    )
}
