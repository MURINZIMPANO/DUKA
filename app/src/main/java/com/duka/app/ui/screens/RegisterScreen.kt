package com.duka.app.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.outlined.BakeryDining
import androidx.compose.material.icons.outlined.LocalGroceryStore
import androidx.compose.material.icons.outlined.LocalPharmacy
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
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

private data class BusinessTypeOption(
    val label: String,
    val icon: ImageVector
)

private val BUSINESS_TYPES = listOf(
    BusinessTypeOption("Supermarket", Icons.Outlined.Storefront),
    BusinessTypeOption("Grocery", Icons.Outlined.LocalGroceryStore),
    BusinessTypeOption("Bakery", Icons.Outlined.BakeryDining),
    BusinessTypeOption("Pharmacy", Icons.Outlined.LocalPharmacy),
    BusinessTypeOption("Other", Icons.Outlined.Storefront)
)

private val RWANDAN_DISTRICTS = listOf(
    "Bugesera", "Gakenke", "Gasabo", "Gatsibo", "Gicumbi",
    "Gisagara", "Huye", "Kamonyi", "Karongi", "Kayonza",
    "Kicukiro", "Kirehe", "Muhanga", "Musanze", "Ngoma",
    "Ngororero", "Nyabihu", "Nyagatare", "Nyamagabe", "Nyamasheke",
    "Nyanza", "Nyarugenge", "Nyaruguru", "Nyarusiza", "Rubavu",
    "Rulindo", "Rusizi", "Rutsiro", "Rwamagana", "Nyarubuye"
)

private val LANGUAGES = listOf("Kinyarwanda", "English", "French")

/**
 * RegisterScreen — handles both Business Owner and Shopper/Client signup.
 *
 * @param signupRole "owner" (default) or "client". Controls which form variant is shown.
 * @param onCreateAccount callback for owner signup (existing behavior, unchanged).
 * @param onCreateClientAccount callback for client signup (new).
 */
@Composable
fun RegisterScreen(
    isLoading: Boolean,
    error: String?,
    signupRole: String = "owner",
    onCreateAccount: (
        ownerName: String,
        phoneOrEmail: String,
        password: String,
        businessName: String,
        businessType: String,
        employeeCount: Int,
        language: String,
        district: String
    ) -> Unit,
    onCreateClientAccount: ((fullName: String, phoneOrEmail: String, password: String, language: String) -> Unit)? = null,
    onGoToLogin: () -> Unit
) {
    // Common identity fields
    var ownerName by remember { mutableStateOf("") }
    var phoneOrEmail by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Business fields (owner only)
    var businessName by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }
    var employeeCount by remember { mutableIntStateOf(1) }
    var language by remember { mutableStateOf("English") }
    var district by remember { mutableStateOf("") }

    // Dropdown states
    var typeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }
    var districtExpanded by remember { mutableStateOf(false) }

    // Password validation
    val passwordMatch = password == confirmPassword
    val passwordStrong = password.length >= 6
    val passwordError = when {
        confirmPassword.isNotBlank() && !passwordMatch -> "Passwords do not match"
        password.isNotBlank() && !passwordStrong -> "Password must be at least 6 characters"
        else -> null
    }

    val isClient = signupRole == "client"

    val isValid = if (isClient) {
        ownerName.isNotBlank() && phoneOrEmail.isNotBlank() &&
                password.isNotBlank() && confirmPassword.isNotBlank() &&
                passwordMatch && passwordStrong
    } else {
        ownerName.isNotBlank() && phoneOrEmail.isNotBlank() &&
                password.isNotBlank() && confirmPassword.isNotBlank() &&
                passwordMatch && passwordStrong &&
                businessName.isNotBlank() && selectedType.isNotBlank() && district.isNotBlank()
    }

    var hasAppeared by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { hasAppeared = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.AUTH,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Header
            AnimatedVisibility(
                visible = hasAppeared,
                enter = fadeIn(tween(400)) + slideInVertically(tween(400, easing = FastOutSlowInEasing))
            ) {
                Column {
                    Text(
                        text = if (isClient) stringResource(R.string.client_register_title)
                               else stringResource(R.string.register_title),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Forest
                        )
                    )
                    Text(
                        text = if (isClient) stringResource(R.string.client_register_subtitle)
                               else stringResource(R.string.register_subtitle),
                        style = MaterialTheme.typography.bodyLarge.copy(color = OnSurfaceVariant)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                        style = MaterialTheme.typography.bodyMedium.copy(color = Clay)
                    )
                }
            }

            // Section: Your Details
            Text(
                stringResource(R.string.register_your_details),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            )

            OutlinedTextField(
                value = ownerName,
                onValueChange = { ownerName = it },
                label = { Text(stringResource(R.string.field_full_name)) },
                placeholder = { Text(stringResource(R.string.field_full_name_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Person, contentDescription = null, tint = OnSurfaceVariant) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            OutlinedTextField(
                value = phoneOrEmail,
                onValueChange = { phoneOrEmail = it },
                label = { Text(stringResource(R.string.field_phone_or_email)) },
                placeholder = { Text(stringResource(R.string.field_phone_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Phone, contentDescription = null, tint = OnSurfaceVariant) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text(stringResource(R.string.field_password)) },
                placeholder = { Text(stringResource(R.string.field_password_hint)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = OnSurfaceVariant) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Forest,
                    unfocusedBorderColor = Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text(stringResource(R.string.field_confirm_password)) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null, tint = OnSurfaceVariant) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                isError = passwordError != null,
                supportingText = passwordError?.let { { Text(it, color = Clay) } },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (passwordError != null) Clay else Forest,
                    unfocusedBorderColor = if (passwordError != null) Clay else Mist,
                    focusedContainerColor = White,
                    unfocusedContainerColor = White
                )
            )

            // Language dropdown — shown for both owner and client
            Spacer(modifier = Modifier.height(8.dp))
            ExposedDropdownMenuBox(
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = it }
            ) {
                OutlinedTextField(
                    value = language,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.field_language)) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )
                ExposedDropdownMenu(
                    expanded = languageExpanded,
                    onDismissRequest = { languageExpanded = false }
                ) {
                    LANGUAGES.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                language = option
                                languageExpanded = false
                            }
                        )
                    }
                }
            }

            // === Owner-only business fields below ===
            if (!isClient) {
                Spacer(modifier = Modifier.height(8.dp))

                // Section: Business Details
                Text(
                    stringResource(R.string.register_business_details),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                )

                OutlinedTextField(
                    value = businessName,
                    onValueChange = { businessName = it },
                    label = { Text(stringResource(R.string.field_business_name)) },
                    placeholder = { Text(stringResource(R.string.field_business_name_hint)) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Forest,
                        unfocusedBorderColor = Mist,
                        focusedContainerColor = White,
                        unfocusedContainerColor = White
                    )
                )

                // Icon-labeled business type picker
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedType,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.field_business_type)) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                        leadingIcon = {
                            val selectedOption = BUSINESS_TYPES.find { it.label == selectedType }
                            Icon(
                                selectedOption?.icon ?: Icons.Outlined.Storefront,
                                contentDescription = null,
                                tint = Forest
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Forest,
                            unfocusedBorderColor = Mist,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        BUSINESS_TYPES.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option.label) },
                                leadingIcon = {
                                    Icon(option.icon, contentDescription = null, tint = Forest)
                                },
                                onClick = {
                                    selectedType = option.label
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }

                // Employee count stepper
                Text(stringResource(R.string.register_employee_count), style = MaterialTheme.typography.labelLarge)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = { if (employeeCount > 1) employeeCount-- },
                        enabled = employeeCount > 1,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("\u2212", style = MaterialTheme.typography.headlineSmall)
                    }
                    Text(
                        text = "$employeeCount",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Forest
                        )
                    )
                    OutlinedButton(
                        onClick = { if (employeeCount < 100) employeeCount++ },
                        enabled = employeeCount < 100,
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text("+", style = MaterialTheme.typography.headlineSmall)
                    }
                }

                // District dropdown
                ExposedDropdownMenuBox(
                    expanded = districtExpanded,
                    onExpandedChange = { districtExpanded = it }
                ) {
                    OutlinedTextField(
                        value = district,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text(stringResource(R.string.field_district)) },
                        trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Forest,
                            unfocusedBorderColor = Mist,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = districtExpanded,
                        onDismissRequest = { districtExpanded = false }
                    ) {
                        RWANDAN_DISTRICTS.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    district = option
                                    districtExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Primary CTA
            DukaPrimaryButton(
                text = if (isLoading) stringResource(R.string.register_loading)
                       else if (isClient) stringResource(R.string.client_register_button)
                       else stringResource(R.string.register_button),
                onClick = {
                    if (isClient) {
                        onCreateClientAccount?.invoke(
                            ownerName.trim(),
                            phoneOrEmail.trim(),
                            password,
                            language
                        )
                    } else {
                        onCreateAccount(
                            ownerName.trim(),
                            phoneOrEmail.trim(),
                            password,
                            businessName.trim(),
                            selectedType,
                            employeeCount,
                            language,
                            district
                        )
                    }
                },
                enabled = isValid && !isLoading
            )

            // Back to login
            TextButton(
                onClick = onGoToLogin,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    stringResource(R.string.register_back_to_login),
                    style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
