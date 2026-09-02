package com.duka.employee.management

/**
 * EM1: Employee List Screen
 * Owner's entry point for managing their team.
 * Accessible from: Owner → More → "Manage employees"
 *
 * Features:
 * - Header with back arrow and PersonAdd button
 * - Employee cards with name, role chip, code, last seen
 * - MoreVert bottom sheet with Edit Role, Deactivate/Reactivate, Remove
 * - Deactivated employees shown at bottom with reduced opacity
 * - Empty state
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.ManageAccounts
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.PersonAdd
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.Employee
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmployeeListScreen(
    viewModel: EmployeeManagementViewModel,
    onBack: () -> Unit,
    onAddEmployee: () -> Unit
) {
    val listState by viewModel.listState.collectAsState()
    var selectedEmployee by remember { mutableStateOf<Employee?>(null) }
    var showMoreSheet by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }
    var showEditRoleSheet by remember { mutableStateOf(false) }

    // Sort: active first, then inactive
    val sortedEmployees = remember(listState.employees) {
        listState.employees.sortedBy { if (it.isActive) 0 else 1 }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            "Your team",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = White.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        )
                        Text("Employees", color = White, style = MaterialTheme.typography.headlineMedium)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                    }
                },
                actions = {
                    IconButton(onClick = onAddEmployee) {
                        Icon(
                            Icons.Outlined.PersonAdd,
                            contentDescription = "Add employee",
                            tint = Amber
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Forest)
            )
        }
    ) { padding ->
        if (sortedEmployees.isEmpty() && !listState.isLoading) {
            // Empty state
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Canvas)
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.Group,
                        contentDescription = "No employees",
                        modifier = Modifier.size(48.dp),
                        tint = OnSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "No employees yet",
                        style = MaterialTheme.typography.headlineSmall.copy(color = OnSurfaceVariant)
                    )
                    Text(
                        "Add your first team member",
                        style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Canvas)
                    .padding(padding),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(sortedEmployees, key = { it.id }) { employee ->
                    val alpha = if (employee.isActive) 1f else 0.5f
                    EmployeeCard(
                        employee = employee,
                        alpha = alpha,
                        onMoreClick = {
                            selectedEmployee = employee
                            showMoreSheet = true
                        },
                        modifier = Modifier.alpha(alpha)
                    )
                }
            }
        }
    }

    // MoreVert bottom sheet
    if (showMoreSheet && selectedEmployee != null) {
        val emp = selectedEmployee!!
        ModalBottomSheet(
            onDismissRequest = { showMoreSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Employee name header
                Text(
                    emp.name,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Edit role
                Text(
                    "Edit role",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMoreSheet = false
                            showEditRoleSheet = true
                        }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Forest)
                )
                Divider()

                // Deactivate / Reactivate
                Text(
                    if (emp.isActive) "Deactivate" else "Reactivate",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMoreSheet = false
                            viewModel.toggleActive(emp.id, !emp.isActive)
                        }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = if (emp.isActive) Clay else Forest
                    )
                )
                Divider()

                // Remove employee
                Text(
                    "Remove employee",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            showMoreSheet = false
                            showRemoveDialog = true
                        }
                        .padding(12.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(color = Clay)
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    // Edit Role bottom sheet (EM3)
    if (showEditRoleSheet && selectedEmployee != null) {
        val emp = selectedEmployee!!
        EditRoleSheet(
            employee = emp,
            onDismiss = { showEditRoleSheet = false },
            onSave = { newRole ->
                viewModel.editEmployeeRole(emp.id, newRole)
                showEditRoleSheet = false
            }
        )
    }

    // Remove confirmation dialog
    if (showRemoveDialog && selectedEmployee != null) {
        val emp = selectedEmployee!!
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = { Text("Remove employee?") },
            text = { Text("Are you sure you want to remove ${emp.name}? This cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.removeEmployee(emp.id)
                    showRemoveDialog = false
                }) {
                    Text("Remove", color = Clay)
                }
            },
            dismissButton = {
                TextButton(onClick = { showRemoveDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun EmployeeCard(
    employee: Employee,
    alpha: Float,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val roleColor = when (employee.role) {
        "manager" -> Forest
        "stockist" -> Amber
        else -> Mist
    }
    val roleTextColor = when (employee.role) {
        "manager" -> White
        "stockist" -> Ink
        else -> OnSurfaceVariant
    }

    val lastSeenText = remember(employee.lastSeenAt) {
        employee.lastSeenAt?.let { ts ->
            val now = System.currentTimeMillis()
            val diff = now - ts
            when {
                diff < 60_000 -> "Just now"
                diff < 3_600_000 -> "${diff / 60_000}m ago"
                diff < 86_400_000 -> {
                    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
                    "Today, ${sdf.format(Date(ts))}"
                }
                else -> {
                    val days = diff / 86_400_000
                    "$days day${if (days > 1) "s" else ""} ago"
                }
            }
        } ?: "Never logged in"
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar placeholder
            Icon(
                Icons.Filled.AccountCircle,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = Forest
            )
            Spacer(modifier = Modifier.width(12.dp))

            // Center column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    employee.name,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                )
                Spacer(modifier = Modifier.height(2.dp))
                // Role chip
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = roleColor.copy(alpha = if (employee.role == "manager") 1f else 0.15f)
                ) {
                    Text(
                        employee.role.replaceFirstChar { it.uppercase() },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = roleTextColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    "Code: ${employee.code}",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                        color = OnSurfaceVariant
                    )
                )
            }

            // Right side: last seen + more button
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    lastSeenText,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (employee.lastSeenAt != null) OnSurfaceVariant else OnSurfaceVariant.copy(alpha = 0.6f)
                    )
                )
                IconButton(
                    onClick = onMoreClick,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Outlined.MoreVert,
                        contentDescription = "More options",
                        modifier = Modifier.size(18.dp),
                        tint = OnSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditRoleSheet(
    employee: Employee,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var selectedRole by remember { mutableStateOf(employee.role) }
    val roles = listOf("cashier" to "Cashier", "stockist" to "Stockist", "manager" to "Manager")

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                "Edit role",
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                employee.name,
                style = MaterialTheme.typography.bodyMedium.copy(color = OnSurfaceVariant)
            )

            // Current role chip
            val currentColor = when (employee.role) {
                "manager" -> Forest
                "stockist" -> Amber
                else -> OnSurfaceVariant
            }
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = currentColor.copy(alpha = 0.1f)
            ) {
                Text(
                    "Current: ${employee.role.replaceFirstChar { it.uppercase() }}",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium.copy(color = currentColor)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Role picker — segmented button row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                roles.forEach { (value, label) ->
                    val isSelected = selectedRole == value
                    val chipColor = when (value) {
                        "manager" -> Forest
                        "stockist" -> Amber
                        else -> OnSurfaceVariant
                    }
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedRole = value },
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) chipColor else White,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp, if (isSelected) chipColor else Mist
                        )
                    ) {
                        Text(
                            label,
                            modifier = Modifier.padding(vertical = 10.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) White else Ink
                            ),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            androidx.compose.material3.Button(
                onClick = { onSave(selectedRole) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Save changes", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
