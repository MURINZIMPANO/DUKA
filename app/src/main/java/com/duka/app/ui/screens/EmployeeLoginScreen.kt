package com.duka.app.ui.screens

/**
 * Employee Login Screen (V5) — Code-only login.
 *
 * The employee enters their 6-character code and the app resolves
 * which business to scope their session to automatically.
 * No businessId typing, no ambiguity.
 */

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.tween
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DecorativeDensity
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

@Composable
fun EmployeeLoginScreen(
    isLoading: Boolean,
    error: String?,
    onEmployeeLogin: (businessCode: String) -> Unit,
    onGoToLogin: () -> Unit
) {
    var businessCode by remember { mutableStateOf("") }
    var codeVisible by remember { mutableStateOf(true) }
    var hasAppeared by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { hasAppeared = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            density = DecorativeDensity.Low,
            seed = 3,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            AnimatedVisibility(
                visible = hasAppeared,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400, easing = androidx.compose.animation.core.FastOutSlowInEasing))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Filled.Badge,
                        contentDescription = "Employee Login",
                        modifier = Modifier.size(56.dp),
                        tint = Forest
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.emp_login_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Forest
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Enter your employee code to sign in",
                        style = MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Error display
            if (error != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Clay.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium.copy(color = Clay),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Employee code field — large, centered, monospaced
            OutlinedTextField(
                value = businessCode,
                onValueChange = { businessCode = it.uppercase() },
                label = { Text("Employee code") },
                placeholder = { Text("A7X2K1") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                textStyle = MaterialTheme.typography.headlineMedium.copy(
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 3.sp,
                    textAlign = TextAlign.Center
                ),
                leadingIcon = {
                    Icon(Icons.Filled.Badge, contentDescription = null, tint = OnSurfaceVariant)
                },
                trailingIcon = {
                    IconButton(onClick = { codeVisible = !codeVisible }) {
                        Icon(
                            if (codeVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (codeVisible) "Hide code" else "Show code",
                            tint = OnSurfaceVariant
                        )
                    }
                },
                visualTransformation = if (codeVisible) {
                    androidx.compose.ui.text.input.VisualTransformation.None
                } else {
                    androidx.compose.ui.text.input.PasswordVisualTransformation()
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            DukaPrimaryButton(
                text = if (isLoading) stringResource(R.string.emp_login_loading) else "Log in",
                onClick = { onEmployeeLogin(businessCode) },
                enabled = businessCode.isNotBlank() && !isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = onGoToLogin) {
                Text(
                    "I'm a business owner instead",
                    style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                )
            }
        }
    }
}
