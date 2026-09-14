package com.leo.clean_mvvm_mvi.core.designsystem.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.leo.clean_mvvm_mvi.core.designsystem.theme.MaterialThemeDimens

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(MaterialThemeDimens.buttonHeight),
        enabled = enabled && !isLoading,
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        AnimatedContent(
            targetState = isLoading,
            transitionSpec = {
                (
                        fadeIn(
                            animationSpec = tween(180)
                        ) +
                                scaleIn(
                                    initialScale = 0.8f,
                                    animationSpec = tween(180)
                                )
                        ).togetherWith(
                        fadeOut(
                            animationSpec = tween(120)
                        ) +
                                scaleOut(
                                    targetScale = 0.8f,
                                    animationSpec = tween(120)
                                )
                    ) using SizeTransform(
                    clip = false
                )
            },
            label = "primaryButtonContent",
        ) { loading ->

            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(
                        MaterialThemeDimens.iconSizeMedium
                    ),
                    color = contentColor,
                    strokeWidth = MaterialThemeDimens.paddingExtraSmall,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
