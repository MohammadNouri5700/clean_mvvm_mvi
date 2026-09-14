package com.leo.clean_mvvm_mvi.feature.auth.ui

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.leo.clean_mvvm_mvi.core.designsystem.component.BottomSheet
import com.leo.clean_mvvm_mvi.core.designsystem.component.EdgeNotificationState
import com.leo.clean_mvvm_mvi.core.designsystem.component.OneUiEdgeNotificationHost
import com.leo.clean_mvvm_mvi.core.designsystem.component.PrimaryButton
import com.leo.clean_mvvm_mvi.core.designsystem.component.SwitchText
import com.leo.clean_mvvm_mvi.core.designsystem.component.TextField
import com.leo.clean_mvvm_mvi.core.designsystem.component.rememberEdgeNotificationState
import com.leo.clean_mvvm_mvi.core.designsystem.theme.AppTheme
import java.util.Locale
import com.leo.clean_mvvm_mvi.core.designsystem.R as DesignSystemR
import com.leo.clean_mvvm_mvi.feature.auth.R as AuthR

@Composable
fun LoginScreen(
    onNavigateToHome: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val edgeNotificationState = rememberEdgeNotificationState()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is LoginEvent.ShowSnackbar -> {
                    edgeNotificationState.showNotification(
                        type = event.type,
                        message = event.message
                    )
                }
                is LoginEvent.Navigate -> {
                    onNavigateToHome()
                }
            }
        }
    }

    LoginScreenContent(
        state = state,
        edgeNotificationState = edgeNotificationState,
        onAction = viewModel::onAction
    )
}

@Composable
fun LoginScreenContent(
    state: LoginState,
    edgeNotificationState: EdgeNotificationState,
    onAction: (LoginAction) -> Unit
) {
    val isPersian = state.selectedLanguage.equals("FA", ignoreCase = true) ||
            state.selectedLanguage.equals("fa", ignoreCase = true)
    val layoutDirection = if (isPersian) LayoutDirection.Rtl else LayoutDirection.Ltr
    val locale = remember(isPersian) { if (isPersian) Locale("fa") else Locale("en") }

    val currentConfig = LocalConfiguration.current
    val configuration = remember(currentConfig, locale) {
        Configuration(currentConfig).apply {
            setLocale(locale)
            setLayoutDirection(locale)
        }
    }
    val context = LocalContext.current
    val localizedContext = remember(context, configuration) {
        context.createConfigurationContext(configuration)
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
        LocalConfiguration provides configuration,
        LocalContext provides localizedContext
    ) {
        OneUiEdgeNotificationHost(state = edgeNotificationState) {
            Scaffold(
                contentWindowInsets = WindowInsets(0, 0, 0, 0)
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                        .padding(innerPadding)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Header Layout
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .statusBarsPadding()
                                .padding(top = 24.dp, bottom = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(id = DesignSystemR.drawable.ic_trip_white),
                                contentDescription = "Dot One Trip Logo",
                                modifier = Modifier.size(width = 150.dp, height = 70.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = stringResource(id = AuthR.string.welcome_text),
                                color = Color.White,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }

                        // Car Image Area
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = DesignSystemR.drawable.car_image),
                                contentDescription = "Car Header",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp, 0.dp, 0.dp, 300.dp),
                                contentScale = ContentScale.Crop,
                                alignment = Alignment.Center,
                            )
                        }
                    }

                    // Bottom Login Sheet Container
                    BottomSheet {
                        // Theme & Language Switches Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Theme Selector (Sun / Moon)
                            Row(
                                modifier = Modifier
                                    .width(76.dp)
                                    .height(40.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFF2F2F7))
                                    .padding(2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                val isLightSelected = !state.isDarkMode
                                val sunBgColor by animateColorAsState(
                                    if (isLightSelected) Color.White else Color.Transparent,
                                    label = "sunBg"
                                )
                                val moonBgColor by animateColorAsState(
                                    if (!isLightSelected) Color.Black else Color.Transparent,
                                    label = "moonBg"
                                )

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(sunBgColor)
                                        .clickable { onAction(LoginAction.ThemeToggled(isDarkMode = false)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = DesignSystemR.drawable.ic_sun_white),
                                        contentDescription = "Sun Theme",
                                        tint = if (isLightSelected) Color.Black else Color(0xFF8E8E93),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .fillMaxHeight()
                                        .clip(CircleShape)
                                        .background(moonBgColor)
                                        .clickable { onAction(LoginAction.ThemeToggled(isDarkMode = true)) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(id = DesignSystemR.drawable.ic_moon),
                                        contentDescription = "Moon Theme",
                                        tint = if (!isLightSelected) Color.White else Color(0xFF8E8E93),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }

                            // Language Selector Button
                            SwitchText(
                                text = state.selectedLanguage,
                                onClick = { onAction(LoginAction.LanguageToggled) }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Prompt Header Label
                        Text(
                            text = stringResource(id = AuthR.string.enter_your_phone_number),
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        TextField(
                            value = state.phoneNumber,
                            onValueChange = { onAction(LoginAction.PhoneNumberChanged(it)) },
                            hint = stringResource(id = AuthR.string.phone_number_hint),
                            iconResId = DesignSystemR.drawable.ic_phone,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Get OTP Primary Button
                        PrimaryButton(
                            text = stringResource(id = AuthR.string.send_otp),
                            onClick = { onAction(LoginAction.SendOtpClicked) },
                            isLoading = state.isLoading,
                            containerColor = Color.Black,
                            contentColor = Color.White,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Privacy Policy & Terms
                        Text(
                            text = stringResource(id = AuthR.string.privacy_and_policy),
                            color = Color(0xFF8E8E93),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    // Loading Progress Overlay
                    if (state.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.Black.copy(alpha = 0.4f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = Color(0xFF00BCD4)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "English LTR", showBackground = true)
@Composable
private fun LoginScreenContentPreviewEn() {
    AppTheme {
        LoginScreenContent(
            state = LoginState(selectedLanguage = "EN"),
            edgeNotificationState = rememberEdgeNotificationState(),
            onAction = {}
        )
    }
}

@Preview(name = "Persian RTL", showBackground = true)
@Composable
private fun LoginScreenContentPreviewFa() {
    AppTheme {
        LoginScreenContent(
            state = LoginState(selectedLanguage = "FA"),
            edgeNotificationState = rememberEdgeNotificationState(),
            onAction = {}
        )
    }
}

