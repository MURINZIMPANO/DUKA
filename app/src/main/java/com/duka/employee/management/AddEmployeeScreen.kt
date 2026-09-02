package com.duka.employee.management

/**
 * EM2: Add Employee Screen
 * Owner creates a new team member with:
 * - Full name (text field)
 * - Role (segmented button: Cashier / Stockist / Manager)
 * - Auto-generated 6-character code with refresh button
 * - "Add to team" button → success bottom sheet with copyable code
 */

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Inventory
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.PointOfSale
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEmployeeScreen(
    viewModel: EmployeeManagementViewModel,
    onBack: () -> Unit
) {
    val addState by viewModel.addState.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "New team member",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        )
                        Text("Add employee", color = White, style = MaterialTheme.typography.headlineMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Canvas)
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Employee name field
            OutlinedTextField(
                value = addState.name,
                onValueChange = { viewModel.updateName(it) },
                label = { Text("Employee full name") },
                placeholder = { Text("e.g. Jean Niyomwungeri") },
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

            // Role picker — segmented button row
            Text(
                "Role",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val roles = listOf(
                    Triple("cashier", "Cashier", Icons.Outlined.PointOfSale),
                    Triple("stockist", "Stockist", Icons.Outlined.Inventory),
                    Triple("manager", "Manager", Icons.Outlined.ManageAccounts)
                )
                roles.forEach { (value, label, icon) ->
                    val isSelected = addState.role == value
                    val chipColor = when (value) {
                        "manager" -> Forest
                        "stockist" -> Amber
                        else -> OnSurfaceVariant
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { viewModel.updateRole(value) },
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) chipColor else White
                        ),
                        border = BorderStroke(1.dp, if (isSelected) chipColor else Mist)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                icon,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = if (isSelected) White else chipColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                label,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) White else Ink
                                )
                            )
                        }
                    }
                }
            }

            // Employee code (auto-generated, read-only)
            Text(
                "Employee code",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
            )
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = White)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        addState.generatedCode,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp
                        )
                    )
                    IconButton(onClick = { viewModel.regenerateCode() }) {
                        Icon(
                            Icons.Outlined.Refresh,
                            contentDescription = "Regenerate code",
                            tint = Forest
                        )
                    }
                }
            }
            Text(
                "This 6-character code is unique to this employee. Share it so they can log in.",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error display
            addState.error?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Clay.copy(alpha = 0.1f))
                ) {
                    Text(
                        error,
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(color = Clay)
                    )
                }
            }

            // Add to team button
            Button(
                onClick = { viewModel.addEmployee() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = addState.name.isNotBlank() && !addState.isSubmitting
            ) {
                Text(
                    if (addState.isSubmitting) "Adding…" else "Add to team",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }

    // Success bottom sheet
    if (addState.showSuccess) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissAddSuccess() },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    "Employee added",
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    "Their code is",
                    style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                )
                Text(
                    addState.successCode,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp,
                        color = Forest
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Copy button
                TextButton(onClick = {
                    scope.launch {
                        clipboardManager.setText(AnnotatedString(addState.successCode))
                    }
                }) {
                    Icon(
                        Icons.Outlined.ContentCopy,
                        contentDescription = "Copy code",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Copy code")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { viewModel.dismissAddSuccess() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Done", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
