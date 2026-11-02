package com.saiful.tmdbexplorer.welcome

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saiful.base.ui.theme.CinemaCrimson
import com.saiful.base.ui.theme.CrimsonBright
import com.saiful.base.ui.theme.CrimsonDark
import com.saiful.base.ui.theme.CrimsonLight
import com.saiful.base.ui.theme.GlassBorder
import com.saiful.base.ui.theme.NeonAmber
import com.saiful.base.ui.theme.ObsidianCanvas
import com.saiful.base.ui.theme.OnSurfaceDark
import com.saiful.base.ui.theme.OnSurfaceVariantDark
import com.saiful.base.ui.theme.OutlineDark
import com.saiful.base.ui.theme.SecondaryContainer
import com.saiful.base.ui.theme.SecondaryLight
import com.saiful.base.ui.theme.SurfaceContainerHighDark
import com.saiful.base.ui.theme.SurfaceContainerLowestDark
import com.saiful.base.ui.theme.SurfaceVariantDark
import com.saiful.base.ui.theme.TextMuted

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WelcomeScreen(
    onGetStarted: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Ambient breathing animations
    val infiniteTransition = rememberInfiniteTransition(label = "WelcomeAnimations")
    
    val pulseGlowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.70f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "PulseGlowAlpha"
    )

    val emblemScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EmblemScale"
    )

    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, delayMillis = 0, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Dot1Alpha"
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, delayMillis = 200, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Dot2Alpha"
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, delayMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Dot3Alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ObsidianCanvas)
    ) {
        // --- Ambient Background Photonic Glows ---
        // Top Crimson Radial Aura
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .size(360.dp)
                .blur(90.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CinemaCrimson.copy(alpha = 0.28f * pulseGlowAlpha),
                            Color.Transparent
                        )
                    )
                )
        )

        // Mid-Right Amber Accent Glow
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .size(280.dp)
                .blur(80.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            SecondaryContainer.copy(alpha = 0.12f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Bottom-Left Crimson Secondary Glow
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(300.dp)
                .blur(85.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            CinemaCrimson.copy(alpha = 0.18f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Main Screen Content Container
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .windowInsetsPadding(WindowInsets.navigationBars)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // --- 1. Top Cinematic Micro-Header Badge ---
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(SurfaceContainerHighDark.copy(alpha = 0.75f))
                    .border(1.dp, GlassBorder, CircleShape)
                    .padding(horizontal = 14.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Film reel emblem in badge
                Canvas(modifier = Modifier.size(15.dp)) {
                    val w = size.width
                    val h = size.height
                    drawRoundRect(
                        color = CinemaCrimson,
                        size = size,
                        cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                    drawCircle(color = CinemaCrimson, radius = 1.2.dp.toPx(), center = Offset(w * 0.32f, h * 0.5f))
                    drawCircle(color = CinemaCrimson, radius = 1.2.dp.toPx(), center = Offset(w * 0.68f, h * 0.5f))
                }
                Text(
                    text = "FILM DATABASE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.6.sp,
                        color = OnSurfaceDark
                    )
                )
            }

            // --- 2. Center Presentation: Reel Monogram & Brand Typography ---
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Monogram with Photonic Glow Aura
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(emblemScale),
                    contentAlignment = Alignment.Center
                ) {
                    // Blurred glow layer behind monogram
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .blur(22.dp)
                            .background(
                                CinemaCrimson.copy(alpha = pulseGlowAlpha * 0.75f),
                                CircleShape
                            )
                    )

                    // Film Reel Disc & Play Monogram Canvas
                    Canvas(modifier = Modifier.size(112.dp)) {
                        val canvasWidth = size.width
                        val canvasHeight = size.height
                        val center = Offset(canvasWidth / 2f, canvasHeight / 2f)
                        val discRadius = canvasWidth / 2f

                        // Outer Disc Frame with Radial Gradient
                        drawCircle(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    SurfaceVariantDark,
                                    SurfaceContainerHighDark,
                                    SurfaceContainerLowestDark
                                ),
                                center = center,
                                radius = discRadius
                            ),
                            radius = discRadius,
                            center = center
                        )

                        // Outer Disc Hairline Border
                        drawCircle(
                            color = GlassBorder,
                            radius = discRadius,
                            center = center,
                            style = Stroke(width = 1.5.dp.toPx())
                        )

                        // Inner Recessed Disc Track
                        val innerRadius = discRadius * 0.82f
                        drawCircle(
                            color = SurfaceContainerLowestDark,
                            radius = innerRadius,
                            center = center
                        )

                        // Film Perforation Pips in Perimeter Track
                        val pipsRadius = discRadius * 0.72f
                        val pipDotRadius = 1.8.dp.toPx()
                        val pipAngles = listOf(30.0, 75.0, 120.0, 165.0, 210.0, 255.0, 300.0, 345.0)
                        for (angleDeg in pipAngles) {
                            val rad = Math.toRadians(angleDeg)
                            val px = center.x + (pipsRadius * Math.cos(rad)).toFloat()
                            val py = center.y + (pipsRadius * Math.sin(rad)).toFloat()
                            drawCircle(
                                color = OutlineDark.copy(alpha = 0.45f),
                                radius = pipDotRadius,
                                center = Offset(px, py)
                            )
                        }

                        // Dashed Cinematic Orbital Wave Curves
                        val arcRadius = discRadius * 0.50f
                        val dashPathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 8f), 0f)

                        // Top-left arc
                        val pathTopArc = Path().apply {
                            addArc(
                                androidx.compose.ui.geometry.Rect(
                                    center.x - arcRadius,
                                    center.y - arcRadius,
                                    center.x + arcRadius,
                                    center.y + arcRadius
                                ),
                                190f,
                                120f
                            )
                        }
                        drawPath(
                            path = pathTopArc,
                            color = CrimsonLight.copy(alpha = 0.85f),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                                pathEffect = dashPathEffect
                            )
                        )

                        // Bottom-right arc
                        val pathBottomArc = Path().apply {
                            addArc(
                                androidx.compose.ui.geometry.Rect(
                                    center.x - arcRadius,
                                    center.y - arcRadius,
                                    center.x + arcRadius,
                                    center.y + arcRadius
                                ),
                                10f,
                                120f
                            )
                        }
                        drawPath(
                            path = pathBottomArc,
                            color = CrimsonLight.copy(alpha = 0.85f),
                            style = Stroke(
                                width = 2.dp.toPx(),
                                cap = StrokeCap.Round,
                                pathEffect = dashPathEffect
                            )
                        )

                        // Center Sharp Prism Play Triangle
                        val trianglePath = Path().apply {
                            val tLeft = center.x - 11.dp.toPx()
                            val tTop = center.y - 18.dp.toPx()
                            val tBottom = center.y + 18.dp.toPx()
                            val tRight = center.x + 18.dp.toPx()

                            moveTo(tLeft, tTop)
                            lineTo(tRight, center.y)
                            lineTo(tLeft, tBottom)
                            close()
                        }

                        drawPath(
                            path = trianglePath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    CrimsonBright,
                                    CinemaCrimson,
                                    CrimsonDark
                                ),
                                startY = center.y - 18.dp.toPx(),
                                endY = center.y + 18.dp.toPx()
                            ),
                            style = Fill
                        )

                        // Ambient Optical Flare Dot at Top-Right
                        val flareCenter = Offset(
                            center.x + discRadius * 0.65f,
                            center.y - discRadius * 0.65f
                        )
                        drawCircle(
                            color = SecondaryLight,
                            radius = 3.dp.toPx(),
                            center = flareCenter
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Wordmark: CineWave
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Cine",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceDark,
                            letterSpacing = (-0.5).sp
                        )
                    )
                    Text(
                        text = "Wave",
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = CinemaCrimson,
                            letterSpacing = (-0.5).sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tagline
                Text(
                    text = "Explore Movies, TV Series, Cast & Ratings. Your Ultimate Cinema Guide.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = OnSurfaceVariantDark,
                        lineHeight = 21.sp,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 24.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Feature Showcase Pills
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FeaturePill(
                        icon = Icons.Default.Person,
                        iconTint = CinemaCrimson,
                        label = "Cast & Crew Info"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    FeaturePill(
                        icon = Icons.Default.Star,
                        iconTint = NeonAmber,
                        label = "Ratings & Reviews"
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    FeaturePill(
                        icon = Icons.Default.PlayArrow,
                        iconTint = CrimsonLight,
                        label = "Official Trailers"
                    )
                }
            }

            // --- 3. Bottom Controls & Loading Cue ---
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Primary Glowing Crimson "Get Started" Button
                Button(
                    onClick = onGetStarted,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                        .clip(RoundedCornerShape(999.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CinemaCrimson,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 8.dp,
                        pressedElevation = 2.dp
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Get Started",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color.White
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Cinematic Pulse Indicator Dots & Ready Cue
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(CinemaCrimson.copy(alpha = dot1Alpha), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(CinemaCrimson.copy(alpha = dot2Alpha), CircleShape)
                        )
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(CinemaCrimson.copy(alpha = dot3Alpha), CircleShape)
                        )
                    }

                    Text(
                        text = "CINEMA DATABASE READY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = TextMuted,
                            letterSpacing = 2.sp,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturePill(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    label: String
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(999.dp))
            .background(SurfaceContainerHighDark.copy(alpha = 0.80f))
            .border(1.dp, GlassBorder, RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                color = OnSurfaceDark,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp
            )
        )
    }
}
