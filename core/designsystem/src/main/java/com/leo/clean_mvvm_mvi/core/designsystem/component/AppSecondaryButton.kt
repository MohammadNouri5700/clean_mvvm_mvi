package com.leo.clean_mvvm_mvi.core.designsystem.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.leo.clean_mvvm_mvi.core.designsystem.theme.MaterialThemeDimens

@Composable
fun AppSecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 44.dp,
    containerColor: Color = Color(0xFFF2F2F7),
    contentColor: Color = Color.Black,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(height),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(MaterialThemeDimens.iconSizeMedium),
                color = contentColor,
                strokeWidth = MaterialThemeDimens.paddingExtraSmall,
            )
        } else {
            Text(
                text = text,
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
fun SecondButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    height: Dp = 44.dp,
    containerColor: Color = Color(0xFFF2F2F7),
    contentColor: Color = Color.Black,
) {
    AppSecondaryButton(
        text = text,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        isLoading = isLoading,
        height = height,
        containerColor = containerColor,
        contentColor = contentColor,
    )
}

