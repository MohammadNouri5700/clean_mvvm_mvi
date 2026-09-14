package com.leo.clean_mvvm_mvi.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animate
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.min

/**
 * Optimized shape:
 * - A single instance is maintained
 * - Path is reused
 * - bulge is read via provider -> no new Shape created per frame
 */
private class BubbleBottomSheetShape(
    private val bulgeProvider: () -> Float,
    private val cornerRadiusPx: Float,
) : Shape {

    private val path = Path()

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        path.reset()

        val width = size.width
        val height = size.height
        val radius = cornerRadiusPx.coerceAtMost(min(width, height) * 0.5f)
        val bulge = bulgeProvider()

        if (bulge < 1f) {
            path.addRoundRect(
                RoundRect(
                    left = 0f,
                    top = 0f,
                    right = width,
                    bottom = height,
                    topLeftCornerRadius = CornerRadius(radius),
                    topRightCornerRadius = CornerRadius(radius),
                )
            )
            return Outline.Generic(path)
        }

        val controlY = -bulge

        path.moveTo(0f, height)
        path.lineTo(0f, radius)
        path.quadraticBezierTo(0f, 0f, radius, 0f)
        path.cubicTo(
            width * 0.25f, controlY,
            width * 0.75f, controlY,
            width - radius, 0f
        )
        path.quadraticBezierTo(width, 0f, width, radius)
        path.lineTo(width, height)
        path.close()

        return Outline.Generic(path)
    }
}

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun BottomSheet(
    modifier: Modifier = Modifier,
    containerColor: Color = Color.White,
    horizontalPadding: Dp = 20.dp,
    bottomPadding: Dp = 20.dp,
    isExpandable: Boolean = true,
    showDragHandle: Boolean = true,
    content: @Composable ColumnScope.() -> Unit,
) {
    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    val cornerRadiusPx = remember(density) { with(density) { 28.dp.toPx() } }
    val maxBulgePx = remember(density) { with(density) { 20.dp.toPx() } }

    var naturalHeightPx by remember { mutableIntStateOf(0) }
    var sheetHeightPx by remember { mutableFloatStateOf(0f) }
    var animationJob by remember { mutableStateOf<Job?>(null) }

    val dragOffsetPx by remember {
        derivedStateOf {
            max(0f, sheetHeightPx - naturalHeightPx)
        }
    }

    val maxBubbleOverscroll by remember {
        derivedStateOf {
            if (naturalHeightPx > 0) naturalHeightPx * 0.45f else 0f
        }
    }

    val progress by remember {
        derivedStateOf {
            if (maxBubbleOverscroll > 0f) {
                (dragOffsetPx / maxBubbleOverscroll).coerceIn(0f, 1f)
            } else 0f
        }
    }

    val bulgePx by remember {
        derivedStateOf { maxBulgePx * progress }
    }

    val shape = remember {
        BubbleBottomSheetShape(
            bulgeProvider = { bulgePx },
            cornerRadiusPx = cornerRadiusPx
        )
    }

    val dragModifier = if (isExpandable && naturalHeightPx > 0) {
        Modifier.pointerInput(Unit) {
            detectVerticalDragGestures(
                onDragStart = {
                    animationJob?.cancel()
                    animationJob = null
                },
                onVerticalDrag = { change, dragAmount ->
                    change.consume()

                    val overscroll = max(0f, sheetHeightPx - naturalHeightPx)
                    val resistance = 1f - min(
                        1f,
                        overscroll / (maxBubbleOverscroll + 1f)
                    )
                    val effectiveDrag = dragAmount * (0.5f + 0.5f * resistance)

                    sheetHeightPx = (sheetHeightPx - effectiveDrag)
                        .coerceAtLeast(0f)
                },
                onDragEnd = {
                    settleToNatural(
                        scope = scope,
                        start = sheetHeightPx,
                        target = naturalHeightPx.toFloat(),
                        onValue = { sheetHeightPx = it },
                        onJob = { animationJob = it }
                    )
                },
                onDragCancel = {
                    settleToNatural(
                        scope = scope,
                        start = sheetHeightPx,
                        target = naturalHeightPx.toFloat(),
                        onValue = { sheetHeightPx = it },
                        onJob = { animationJob = it }
                    )
                },
            )
        }
    } else {
        Modifier
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom)),
        contentAlignment = Alignment.BottomCenter,
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (sheetHeightPx > 0f) {
                        Modifier.height(with(density) { sheetHeightPx.toDp() })
                    } else Modifier
                )
                .graphicsLayer {
                    val p = progress
                    scaleX = 1f + p * 0.06f
                    scaleY = 1f + p * 0.13f
                }
                .then(dragModifier),
            shape = shape,
            color = containerColor,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (naturalHeightPx == 0) {
                            Modifier.onSizeChanged { size ->
                                if (size.height > 0) {
                                    naturalHeightPx = size.height
                                    if (sheetHeightPx == 0f) {
                                        sheetHeightPx = size.height.toFloat()
                                    }
                                }
                            }
                        } else Modifier
                    ),
            ) {
                if (showDragHandle) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        val handleScale = 1f + progress * 0.3f

                        Box(
                            modifier = Modifier
                                .width(74.dp)
                                .height(8.dp)
                                .scale(handleScale)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E5EA)),
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = horizontalPadding)
                        .padding(bottom = bottomPadding),
                    content = content,
                )
            }
        }
    }
}

private fun settleToNatural(
    scope: kotlinx.coroutines.CoroutineScope,
    start: Float,
    target: Float,
    onValue: (Float) -> Unit,
    onJob: (Job) -> Unit,
) {
    val job = scope.launch {
        animate(
            initialValue = start,
            targetValue = target,
            animationSpec = spring(
                dampingRatio = 0.22f,
                stiffness = Spring.StiffnessMediumLow,
            ),
        ) { value, _ ->
            onValue(value)
        }
    }
    onJob(job)
}
