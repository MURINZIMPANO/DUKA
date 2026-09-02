package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.tween
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DecorativeDensity
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.components.DukaPrimaryButton
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R

@Composable
fun LoginScreen(
    isLoading: Boolean,
    error: String?,
    onLogin: (phoneOrEmail: String, password: String) -> Unit,
    onGoToSignup: () -> Unit,
    onGoToClientSignup: () -> Unit = {},
    onGoToEmployeeLogin: () -> Unit
) {
    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var hasAppeared by remember { mutableStateOf(false) }

    // Staggered entrance animation
    androidx.compose.runtime.LaunchedEffect(Unit) { hasAppeared = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop — subtle line-art motifs behind content
        DecorativeBackdrop(
            page = DukaPage.AUTH,
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
                        imageVector = Icons.Filled.Storefront,
                        contentDescription = "Duka",
                        modifier = Modifier.size(56.dp),
                        tint = Forest
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Forest
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(R.string.login_title),
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

            // Phone/email field
            OutlinedTextField(
                value = phoneOrEmail,
                onValueChange = { phoneOrEmail = it },
                label = { Text(stringResource(R.string.field_phone_or_email)) },
                placeholder = { Text(stringResource(R.string.login_phone_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = {
                    Icon(Icons.Filled.Phone, contentDescription = null, tint = OnSurfaceVariant)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.field_password)) },
                placeholder = { Text(stringResource(R.string.login_password_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Login button
            DukaPrimaryButton(
                text = if (isLoading) stringResource(R.string.login_loading) else stringResource(R.string.login_button),
                onClick = { onLogin(phoneOrEmail, password) },
                enabled = phoneOrEmail.isNotBlank() && password.isNotBlank() && !isLoading
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Demo accounts hint — visible when seed data is present
            var showDemoHint by remember { mutableStateOf(false) }
            TextButton(onClick = { showDemoHint = !showDemoHint }) {
                Text(
                    if (showDemoHint) "Hide demo accounts" else "Demo accounts — remove before publishing",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = OnSurfaceVariant,
                        fontSize = 12.sp
                    )
                )
            }
            AnimatedVisibility(visible = showDemoHint) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Forest.copy(alpha = 0.05f), RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Owner:     0788000001  /  demo1234", style = MaterialTheme.typography.bodySmall.copy(color = Forest, fontWeight = FontWeight.Medium))
                    Text("Employee:  code JB7X2K", style = MaterialTheme.typography.bodySmall.copy(color = Forest, fontWeight = FontWeight.Medium))
                    Text("Client:    0788000002  /  client1234", style = MaterialTheme.typography.bodySmall.copy(color = Forest, fontWeight = FontWeight.Medium))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary links
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                TextButton(onClick = onGoToSignup) {
                    Text(
                        stringResource(R.string.login_signup_link),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                    )
                }
                TextButton(onClick = onGoToClientSignup) {
                    Text(
                        stringResource(R.string.login_client_signup_link),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                    )
                }
                TextButton(onClick = onGoToEmployeeLogin) {
                    Text(
                        stringResource(R.string.login_employee_link),
                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                    )
                }
            }
        }
    }
}
