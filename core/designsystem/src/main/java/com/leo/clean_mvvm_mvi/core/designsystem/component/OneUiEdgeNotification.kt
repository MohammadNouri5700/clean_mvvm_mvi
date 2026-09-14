package com.leo.clean_mvvm_mvi.core.designsystem.component

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Visual states supported by the Samsung One UI Edge Notification component.
 */
enum class EdgeNotificationType(
    val defaultTitle: String,
    val accentColor: Color,
    val glowColor: Color,
    val surfaceTint: Color
) {
    INFO(
        defaultTitle = "Information",
        accentColor = Color(0xFF0088FF), // Samsung One UI Blue Accent
        glowColor = Color(0xFF00D2FF),
        surfaceTint = Color(0x0F0088FF)
    ),
    WARNING(
        defaultTitle = "Warning",
        accentColor = Color(0xFFFF9500), // Samsung Galaxy Warm Amber/Orange
        glowColor = Color(0xFFFFC107),
        surfaceTint = Color(0x0FFF9500)
    ),
    ERROR(
        defaultTitle = "Connection Alert",
        accentColor = Color(0xFFFF3B30), // Samsung Coral Red
        glowColor = Color(0xFFFF6B6B),
        surfaceTint = Color(0x0FFF3B30)
    )
}

/**
 * Data holder for an edge notification request.
 */
data class EdgeNotificationData(
    val id: Long = System.currentTimeMillis(),
    val type: EdgeNotificationType = EdgeNotificationType.INFO,
    val title: String? = null,
    val message: String,
    val durationMs: Long = 2000L
)

/**
 * State holder for controlling Samsung One UI Edge Notifications.
 */
class EdgeNotificationState {
    var currentNotification by mutableStateOf<EdgeNotificationData?>(null)
        private set

    var isVisible by mutableStateOf(false)
        private set

    fun showNotification(
        type: EdgeNotificationType,
        message: String,
        title: String? = null,
        durationMs: Long = 2000L
    ) {
        currentNotification = EdgeNotificationData(
            id = System.currentTimeMillis(),
            type = type,
            title = title,
            message = message,
            durationMs = durationMs
        )
        isVisible = true
    }

    fun dismiss() {
        isVisible = false
    }

    fun clear() {
        currentNotification = null
        isVisible = false
    }
}

@Composable
fun rememberEdgeNotificationState(): EdgeNotificationState {
    return remember { EdgeNotificationState() }
}

/**
 * Custom vector icon rendering for Samsung One UI Notification states.
 */
@Composable
fun EdgeNotificationIcon(
    type: EdgeNotificationType,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val color = type.accentColor

        when (type) {
            EdgeNotificationType.INFO -> {
                val strokeWidth = w * 0.12f
                // Top Dot
                drawCircle(
                    color = color,
                    radius = w * 0.09f,
                    center = Offset(w * 0.5f, h * 0.28f)
                )
                // Stem
                drawLine(
                    color = color,
                    start = Offset(w * 0.5f, h * 0.45f),
                    end = Offset(w * 0.5f, h * 0.78f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
            EdgeNotificationType.WARNING -> {
                val strokeWidth = w * 0.11f
                val path = Path().apply {
                    moveTo(w * 0.5f, h * 0.12f)
                    lineTo(w * 0.90f, h * 0.85f)
                    lineTo(w * 0.10f, h * 0.85f)
                    close()
                }
                drawPath(
                    path = path,
                    color = color,
                    style = Stroke(
                        width = strokeWidth,
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                )
                // Exclamation line
                drawLine(
                    color = color,
                    start = Offset(w * 0.5f, h * 0.38f),
                    end = Offset(w * 0.5f, h * 0.60f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                // Exclamation dot
                drawCircle(
                    color = color,
                    radius = w * 0.055f,
                    center = Offset(w * 0.5f, h * 0.73f)
                )
            }
            EdgeNotificationType.ERROR -> {
                val strokeWidth = w * 0.12f
                drawCircle(
                    color = color,
                    radius = w * 0.42f,
                    center = Offset(w * 0.5f, h * 0.5f),
                    style = Stroke(width = strokeWidth)
                )
                // Cross lines
                drawLine(
                    color = color,
                    start = Offset(w * 0.33f, h * 0.33f),
                    end = Offset(w * 0.67f, h * 0.67f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = color,
                    start = Offset(w * 0.67f, h * 0.33f),
                    end = Offset(w * 0.33f, h * 0.67f),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )
            }
        }
    }
}

/**
 * Samsung-style Edge Lighting effect around the edges of the display container.
 * Features luminous border, soft outer glow, gradient intensity, and subtle pulsing animation.
 */
@Composable
fun EdgeLightingDisplayOverlay(
    visible: Boolean,
    type: EdgeNotificationType,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 32.dp,
    borderWidth: Dp = 3.dp
) {
    val alphaAnim by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing),
        label = "EdgeLightingAlpha"
    )

    if (alphaAnim <= 0.001f) return

    val infiniteTransition = rememberInfiniteTransition(label = "EdgeLightingPulse")
    val pulseFactor by infiniteTransition.animateFloat(
        initialValue = 0.65f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "EdgeLightingPulseFactor"
    )

    val currentAlpha = alphaAnim * pulseFactor
    val accentColor = type.accentColor
    val glowColor = type.glowColor

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .alpha(currentAlpha)
    ) {
        val strokeWidthPx = borderWidth.toPx()
        val halfStroke = strokeWidthPx / 2f

        val rectSize = Size(
            width = size.width - strokeWidthPx,
            height = size.height - strokeWidthPx
        )

        val topLeft = Offset(halfStroke, halfStroke)
        val cornerRadiusPx = cornerRadius.toPx()

        // ─────────────────────────────────────────────
        // 1. SOFT OUTER GLOW
        // Fades in -> peaks -> fades out to transparent
        // ─────────────────────────────────────────────
        drawRoundRect(
            brush = Brush.sweepGradient(
                colorStops = arrayOf(
                    0.00f to Color.Transparent,
                    0.12f to accentColor.copy(alpha = 0.20f),
                    0.15f to accentColor.copy(alpha = 0.55f),
                    0.10f to Color.White.copy(alpha = 0.85f),
                    0.10f to Color.White,
                    0.10f to Color.White.copy(alpha = 0.85f),
                    0.15f to accentColor.copy(alpha = 0.55f),
                    0.18f to accentColor.copy(alpha = 0.20f),
                    1.00f to Color.Transparent
                ),
                center = Offset(
                    size.width / 2f,
                    size.height / 2f
                )
            ),
            topLeft = topLeft,
            size = rectSize,
            cornerRadius = CornerRadius(
                cornerRadiusPx,
                cornerRadiusPx
            ),
            style = Stroke(
                width = strokeWidthPx * 4.5f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // ─────────────────────────────────────────────
        // 2. SECONDARY SOFT GLOW
        // Slightly tighter and brighter
        // ─────────────────────────────────────────────
        drawRoundRect(
            brush = Brush.sweepGradient(
                colorStops = arrayOf(
                    0.00f to Color.Transparent,
                    0.12f to accentColor.copy(alpha = 0.08f),
                    0.28f to glowColor.copy(alpha = 0.20f),
                    0.45f to glowColor.copy(alpha = 0.34f),
                    0.55f to glowColor.copy(alpha = 0.34f),
                    0.72f to glowColor.copy(alpha = 0.20f),
                    0.88f to accentColor.copy(alpha = 0.08f),
                    1.00f to Color.Transparent
                ),
                center = Offset(
                    size.width * 0.5f,
                    size.height * 0.5f
                )
            ),
            topLeft = topLeft,
            size = rectSize,
            cornerRadius = CornerRadius(
                cornerRadiusPx,
                cornerRadiusPx
            ),
            style = Stroke(
                width = strokeWidthPx * 2.2f,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )

        // ─────────────────────────────────────────────
        // 3. LUMINOUS CORE
        // Also fades toward the ends instead of
        // remaining fully visible everywhere.
        // ─────────────────────────────────────────────
        drawRoundRect(
            brush = Brush.sweepGradient(
                colorStops = arrayOf(
                    0.00f to Color.Transparent,
                    0.10f to accentColor.copy(alpha = 0.35f),
                    0.24f to accentColor.copy(alpha = 0.75f),
                    0.40f to Color.White.copy(alpha = 0.90f),
                    0.50f to Color.White,
                    0.60f to Color.White.copy(alpha = 0.90f),
                    0.76f to accentColor.copy(alpha = 0.75f),
                    0.90f to accentColor.copy(alpha = 0.35f),
                    1.00f to Color.Transparent
                ),
                center = Offset(
                    size.width * 0.5f,
                    size.height * 0.5f
                )
            ),
            topLeft = topLeft,
            size = rectSize,
            cornerRadius = CornerRadius(
                cornerRadiusPx,
                cornerRadiusPx
            ),
            style = Stroke(
                width = strokeWidthPx,
                cap = StrokeCap.Round,
                join = StrokeJoin.Round
            )
        )
    }
}

/**
 * Floating glassmorphic card inspired by Samsung One UI / Galaxy Note aesthetics.
 * Features 22dp rounded corners, semi-transparent glass surface, subtle elevation,
 * leading circular badge, short message, and state-colored accent glow.
 */
@Composable
fun OneUiEdgeNotificationCard(
    type: EdgeNotificationType,
    message: String,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    val displayTitle = title ?: type.defaultTitle
    val isDark = isSystemInDarkTheme()

    val glassBackground = if (isDark) {
        Color(0xEE1C1D22)
    } else {
        Color(0xFAFAFCFF)
    }

    val glassBorder = if (isDark) {
        Color(0x33FFFFFF)
    } else {
        type.accentColor.copy(alpha = 0.22f)
    }

    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(22.dp),
                    spotColor = type.glowColor.copy(alpha = 0.35f),
                    ambientColor = Color.Black.copy(alpha = 0.15f)
                )
                .clip(RoundedCornerShape(22.dp))
                .background(glassBackground)
                .drawBehind {
                    // Soft accent ambient glow behind card
                    drawCircle(
                        color = type.accentColor.copy(alpha = 0.08f),
                        radius = size.width * 0.45f,
                        center = Offset(size.width * 0.15f, size.height * 0.5f)
                    )
                }
                .border(
                    width = 1.dp,
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            type.accentColor.copy(alpha = 0.45f),
                            glassBorder,
                            type.glowColor.copy(alpha = 0.30f)
                        )
                    ),
                    shape = RoundedCornerShape(22.dp)
                )
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Leading Circular Badge
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    type.accentColor.copy(alpha = 0.22f),
                                    type.accentColor.copy(alpha = 0.08f)
                                )
                            )
                        )
                        .border(
                            width = 1.dp,
                            color = type.accentColor.copy(alpha = 0.35f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    EdgeNotificationIcon(
                        type = type,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Notification Text Column
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = displayTitle,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.2).sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Normal,
                            lineHeight = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // One UI signature vertical state accent bar
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(24.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    type.accentColor,
                                    type.glowColor
                                )
                            )
                        )
                )
            }
        }
    }
}

/**
 * Top-level Host for managing Samsung One UI Edge Notifications.
 *
 * Handles smooth sliding entrance from the top toward the screen center,
 * combined fade-in and scale (92% -> 100%), synced Edge Lighting pulse,
 * display duration (~2s), and reverse exit animation.
 */
@Composable
fun OneUiEdgeNotificationHost(
    state: EdgeNotificationState,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 32.dp,
    content: @Composable () -> Unit
) {
    val notification = state.currentNotification
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(notification?.id, state.isVisible) {
        if (state.isVisible && notification != null) {
            // Entrance animation: Slide down, fade in, scale 92% -> 100%
            animProgress.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            // Remain visible for requested duration (~2 seconds)
            delay(notification.durationMs)
            // Exit animation: Slide up, fade out, scale 100% -> 92%
            animProgress.animateTo(
                targetValue = 0f,
                animationSpec = tween(
                    durationMillis = 350,
                    easing = FastOutLinearInEasing
                )
            )
            state.dismiss()
        } else {
            animProgress.snapTo(0f)
        }
    }

    val density = LocalDensity.current

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenHeightPx = constraints.maxHeight.toFloat()
        // Target 1/6 (upper sixth) of the screen height (~16.6% from top)
        val targetTopPx = screenHeightPx * 0.166f
        val offscreenTopPx = with(density) { (-250).dp.toPx() }

        // 1. Background Content (Always fully visible, no dark scrim)
        content()

        // 2. Samsung Edge Lighting Display Border Overlay
        if (notification != null) {
            EdgeLightingDisplayOverlay(
                visible = animProgress.value > 0.01f,
                type = notification.type,
                cornerRadius = cornerRadius
            )
        }

        // 3. Floating Custom Snackbar / Toast Card
        if (notification != null && animProgress.value > 0.001f) {
            val progress = animProgress.value
            val scale = 0.92f + (0.08f * progress)
            val alpha = progress.coerceIn(0f, 1f)

            // Interpolate from off-screen top to 1/3 (upper third) of the screen height
            val currentOffsetYPx = (offscreenTopPx * (1f - progress) + targetTopPx * progress).roundToInt()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(0, currentOffsetYPx) },
                contentAlignment = Alignment.TopCenter
            ) {
                OneUiEdgeNotificationCard(
                    type = notification.type,
                    title = notification.title,
                    message = notification.message,
                    modifier = Modifier
                        .scale(scale)
                        .alpha(alpha)
                )
            }
        }
    }
}

