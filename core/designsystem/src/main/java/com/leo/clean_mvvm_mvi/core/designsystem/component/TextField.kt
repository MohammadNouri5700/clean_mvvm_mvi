package com.leo.clean_mvvm_mvi.core.designsystem.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leo.clean_mvvm_mvi.core.designsystem.R

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
    @DrawableRes iconResId: Int? = R.drawable.ic_phone,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        keyboardType = KeyboardType.Phone,
    ),
    singleLine: Boolean = true,
    enabled: Boolean = true,
    layoutDirection: LayoutDirection = LayoutDirection.Ltr,
    leadingIcon: (@Composable () -> Unit)? = null,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    /*
     * ─────────────────────────────────────────
     * Shape
     * ─────────────────────────────────────────
     *
     * Idle    -> 5.dp
     * Focused -> 20.dp
     */
    val cornerRadius by animateDpAsState(
        targetValue = if (isFocused) 25.dp else 10.dp,
        animationSpec = tween(
            durationMillis = 250,
        ),
        label = "textFieldCornerRadius",
    )

    val fieldHeight by animateDpAsState(
        targetValue = if (isFocused) 58.dp else 48.dp,
        animationSpec = tween(
            durationMillis = 250,
        ),
        label = "textFieldHeight",
    )

    val shape = RoundedCornerShape(cornerRadius)

    /*
     * ─────────────────────────────────────────
     * Colors
     * ─────────────────────────────────────────
     */
    val borderColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFFD1D1D6)
            isFocused -> Color(0xFF00BCD4)
            else -> Color(0xFFD1D1D6)
        },
        animationSpec = tween(220),
        label = "textFieldBorderColor",
    )

    val backgroundColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFFE5E5EA)
            isFocused -> Color(0xFFF8F8FA)
            else -> Color(0xFFF2F2F7)
        },
        animationSpec = tween(220),
        label = "textFieldBackgroundColor",
    )

    val iconColor by animateColorAsState(
        targetValue = when {
            !enabled -> Color(0xFF8E8E93)
            isFocused -> Color(0xFF00BCD4)
            else -> Color(0xFF8E8E93)
        },
        animationSpec = tween(220),
        label = "textFieldIconColor",
    )

    val borderWidth by animateDpAsState(
        targetValue = if (isFocused) 1.5.dp else 1.dp,
        animationSpec = tween(220),
        label = "textFieldBorderWidth",
    )

    /*
     * ─────────────────────────────────────────
     * Icon animation
     * ─────────────────────────────────────────
     */
    val iconScale by animateFloatAsState(
        targetValue = if (isFocused) 1.08f else 1f,
        animationSpec = tween(220),
        label = "textFieldIconScale",
    )

    /*
     * ─────────────────────────────────────────
     * Hint animation
     * ─────────────────────────────────────────
     */
    val hintAlpha by animateFloatAsState(
        targetValue = if (value.isEmpty()) 1f else 0f,
        animationSpec = tween(160),
        label = "textFieldHintAlpha",
    )

    val hintTranslationY by animateFloatAsState(
        targetValue = if (value.isEmpty()) 0f else -4f,
        animationSpec = tween(180),
        label = "textFieldHintTranslation",
    )

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(fieldHeight)
                .clip(shape)
                .background(backgroundColor)
                .border(
                    width = borderWidth,
                    color = borderColor,
                    shape = shape,
                )
                .padding(
                    horizontal = 18.dp,
                )
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            contentAlignment = Alignment.CenterStart,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                /*
                 * Custom leading icon
                 */
                if (leadingIcon != null) {
                    leadingIcon()

                    Spacer(
                        modifier = Modifier.width(14.dp),
                    )
                }

                /*
                 * Default icon
                 */
                else if (iconResId != null) {
                    Icon(
                        painter = painterResource(
                            id = iconResId,
                        ),
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier
                            .size(24.dp)
                            .graphicsLayer {
                                scaleX = iconScale
                                scaleY = iconScale
                            },
                    )

                    Spacer(
                        modifier = Modifier.width(14.dp),
                    )
                }

                Box(
                    modifier = Modifier.weight(1f),
                ) {

                    /*
                     * Hint
                     */
                    if (hint.isNotEmpty()) {
                        Text(
                            text = hint,
                            color = Color(0xFF8E8E93),
                            fontSize = 17.sp,
                            modifier = Modifier.graphicsLayer {
                                alpha = hintAlpha
                                translationY = hintTranslationY
                            },
                        )
                    }

                    /*
                     * Text input
                     */
                    BasicTextField(
                        value = value,
                        onValueChange = onValueChange,
                        enabled = enabled,
                        singleLine = singleLine,
                        textStyle = TextStyle(
                            color = if (enabled) {
                                Color.Black
                            } else {
                                Color(0xFF8E8E93)
                            },
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        keyboardOptions = keyboardOptions,
                        cursorBrush = SolidColor(
                            Color(0xFF00BCD4),
                        ),
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}
