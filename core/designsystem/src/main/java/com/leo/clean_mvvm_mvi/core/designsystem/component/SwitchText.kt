package com.leo.clean_mvvm_mvi.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SwitchText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    width: Dp = 68.dp,
    height: Dp = 40.dp,
    backgroundColor: Color = Color(0xFFF2F2F7),
    textColor: Color = Color.Black,
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = tween(
            durationMillis = 100,
        ),
        label = "switchPressScale",
    )

    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = tween(
            durationMillis = 220,
        ),
        label = "switchBackground",
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .clip(CircleShape)
            .background(animatedBackgroundColor)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick,
            ),
        contentAlignment = Alignment.Center,
    ) {
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                (
                        fadeIn(
                            animationSpec = tween(180),
                        ) +
                                scaleIn(
                                    initialScale = 0.75f,
                                    animationSpec = tween(180),
                                )
                        ).togetherWith(
                        fadeOut(
                            animationSpec = tween(120),
                        ) +
                                scaleOut(
                                    targetScale = 0.75f,
                                    animationSpec = tween(120),
                                )
                    )
            },
            label = "switchText",
        ) { value ->
            Text(
                text = value,
                color = textColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }
}
