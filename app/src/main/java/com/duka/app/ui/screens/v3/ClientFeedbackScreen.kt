package com.duka.app.ui.screens.v3

/**
 * Client Feedback Screen (C5) — "Suggestions & complaints"
 *
 * SCOPE-GUARD: WRITES to V3 FeedbackReport table only. Does NOT touch V1/V2 tables.
 *
 * MOCK STATUS: Saves locally with status "Under review." No real government transmission.
 * Same honesty requirement as V1/V2 Report Issue.
 */

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Help
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.Business
import com.duka.app.data.local.entity.FeedbackReport
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.FeedbackReportRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.Amber
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import kotlinx.coroutines.launch

private data class FeedbackTypeOption(val label: String, val icon: ImageVector)

private val FEEDBACK_TYPES = listOf(
    FeedbackTypeOption("Complaint", Icons.Outlined.Flag),
    FeedbackTypeOption("Suggestion", Icons.Outlined.Lightbulb),
    FeedbackTypeOption("Other", Icons.Outlined.Help)
)

@Composable
fun ClientFeedbackScreen(
    clientUserId: Long,
    feedbackReportRepository: FeedbackReportRepository,
    businessRepository: BusinessRepository? = null,
    onBack: () -> Unit,
    initialAnonymousDefault: Boolean = false
) {
    val scope = rememberCoroutineScope()

    // Form state
    var selectedType by remember { mutableStateOf("") }
    var typeExpanded by remember { mutableStateOf(false) }
    var targetBusiness by remember { mutableStateOf("") }
    var businessExpanded by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var isAnonymous by remember { mutableStateOf(initialAnonymousDefault) }
    var showSuccess by remember { mutableStateOf(false) }

    // Business autocomplete
    val businesses by businessRepository?.getActiveBusiness()?.collectAsState(initial = null)
        ?: remember { mutableStateOf<Business?>(null) }
    val allBusinesses = remember(businesses) {
        businesses?.let { listOf(it) } ?: emptyList()
    }
    val filteredBusinesses = if (targetBusiness.isBlank()) emptyList()
    else allBusinesses.filter { it.name.contains(targetBusiness, ignoreCase = true) }

    // Submissions list
    val submissions by feedbackReportRepository.getByClient(clientUserId).collectAsState(initial = emptyList())

    val isValid = selectedType.isNotBlank() && message.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        // Decorative backdrop
        DecorativeBackdrop(
            page = DukaPage.CLIENT_FEEDBACK,
            modifier = Modifier.fillMaxSize()
        )

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                "Your voice",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = OnSurfaceVariant,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                "Suggestions & complaints",
                                color = White,
                                style = MaterialTheme.typography.headlineMedium
                            )
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
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Type dropdown
                item {
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = it }
                    ) {
                        OutlinedTextField(
                            value = selectedType,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Type") },
                            trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                            leadingIcon = {        val selectedOption = FEEDBACK_TYPES.find { it.label == selectedType }
        Icon(
            selectedOption?.icon ?: Icons.Outlined.Flag,
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
                            FEEDBACK_TYPES.forEach { option ->
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
                }

                // About (target business) with autocomplete
                item {
                    Box {
                        OutlinedTextField(
                            value = targetBusiness,
                            onValueChange = { targetBusiness = it },
                            label = { Text("About (target business)") },
                            placeholder = { Text("Type a shop name...") },
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
                        // Autocomplete dropdown
                        if (filteredBusinesses.isNotEmpty()) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 56.dp),
                                shape = RoundedCornerShape(12.dp),
                                color = White,
                                shadowElevation = 4.dp
                            ) {
                                Column {
                                    filteredBusinesses.forEach { biz ->
                                        Text(
                                            biz.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    targetBusiness = biz.name
                                                    businessExpanded = false
                                                }
                                                .padding(12.dp),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Message field
                item {
                    OutlinedTextField(
                        value = message,
                        onValueChange = { message = it },
                        label = { Text("Message") },
                        placeholder = { Text("Describe your experience...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Forest,
                            unfocusedBorderColor = Mist,
                            focusedContainerColor = White,
                            unfocusedContainerColor = White
                        )
                    )
                }

                // Anonymous toggle
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Anonymous", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = isAnonymous,
                            onCheckedChange = { isAnonymous = it },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = Forest,
                                checkedThumbColor = White,
                                uncheckedTrackColor = Mist
                            )
                        )
                    }
                }

                // Success message
                if (showSuccess) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Forest.copy(alpha = 0.1f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    "Feedback submitted",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = Forest,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Text(
                                    "Status: Under review",
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                        }
                    }
                }

                // Send button
                item {
                    androidx.compose.material3.Button(
                        onClick = {
                            scope.launch {
                                // Saved locally — no real government API integration in this phase
                                feedbackReportRepository.submitReport(
                                    FeedbackReport(
                                        clientUserId = clientUserId,
                                        targetBusinessId = allBusinesses.firstOrNull()?.id ?: 1L,
                                        category = selectedType,
                                        message = message.trim(),
                                        isAnonymous = isAnonymous,
                                        status = "Under review"
                                    )
                                )
                                showSuccess = true
                                selectedType = ""
                                targetBusiness = ""
                                message = ""
                            }
                        },
                        enabled = isValid && !showSuccess,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            "Send to government channel",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }

                // Your submissions
                if (submissions.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Your submissions",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Ink
                            )
                        )
                    }

                    items(submissions) { report ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        "${report.category} \u2014 ${report.message.take(40)}${if (report.message.length > 40) "..." else ""}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                                // Status chip
                                val statusColor = if (report.status == "Under review") Amber else Forest
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = statusColor.copy(alpha = 0.1f)
                                ) {
                                    Text(
                                        report.status,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = statusColor,
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


