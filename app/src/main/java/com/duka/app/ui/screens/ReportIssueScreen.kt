package com.duka.app.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.duka.app.data.local.entity.IssueReport
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.IssueRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Clay
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White
import androidx.compose.ui.res.stringResource
import com.duka.app.R
import kotlinx.coroutines.launch

private data class IssueCategoryOption(
    val label: String,
    val icon: ImageVector
)

private val ISSUE_CATEGORIES = listOf(
    IssueCategoryOption("Tax/EBM", Icons.Outlined.AttachMoney),
    IssueCategoryOption("Supply", Icons.Outlined.LocalShipping),
    IssueCategoryOption("Infrastructure", Icons.Outlined.Build),
    IssueCategoryOption("Other", Icons.Outlined.Flag)
)

@Composable
fun ReportIssueScreen(
    businessRepository: BusinessRepository,
    issueRepository: IssueRepository,
    onBack: () -> Unit
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var category by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var attachBusinessId by remember { mutableStateOf(true) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }

    val isValid = category.isNotBlank() && message.isNotBlank()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.REPORT_ISSUE,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.report_title), color = White) },
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
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                "What's the issue?",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = Ink
                )
            )

            // Icon-labeled category dropdown
            ExposedDropdownMenuBox(
                expanded = categoryExpanded,
                onExpandedChange = { categoryExpanded = it }
            ) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(stringResource(R.string.field_issue_category)) },
                    trailingIcon = { Icon(Icons.Default.ArrowDropDown, "Dropdown") },
                    leadingIcon = {
                        val selectedOption = ISSUE_CATEGORIES.find { it.label == category }
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
                    expanded = categoryExpanded,
                    onDismissRequest = { categoryExpanded = false }
                ) {
                    ISSUE_CATEGORIES.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            leadingIcon = {
                                Icon(option.icon, contentDescription = null, tint = Forest)
                            },
                            onClick = {
                                category = option.label
                                categoryExpanded = false
                            }
                        )
                    }
                }
            }

            OutlinedTextField(
                value = message,
                onValueChange = { message = it },
                label = { Text(stringResource(R.string.field_describe_issue)) },
                placeholder = { Text(stringResource(R.string.field_describe_hint)) },
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

            // Toggle for attaching business ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Attach business ID",
                    style = MaterialTheme.typography.bodyMedium
                )
                Switch(
                    checked = attachBusinessId,
                    onCheckedChange = { attachBusinessId = it },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = Forest,
                        checkedThumbColor = White,
                        uncheckedTrackColor = Mist
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (showSuccess) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Forest.copy(alpha = 0.1f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            "Report submitted",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Forest, fontWeight = FontWeight.Bold)
                        )
                        Text(
                            "Status: Under review. Saved locally.",
                            style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                        )
                    }
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        issueRepository.submitReport(
                            IssueReport(
                                businessId = business?.id ?: 0L,
                                category = category,
                                message = message.trim(),
                                attachBusinessId = attachBusinessId,
                                status = "Under review"
                            )
                        )
                        showSuccess = true
                        category = ""
                        message = ""
                    }
                },
                enabled = isValid && !showSuccess,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Clay,
                    contentColor = White,
                    disabledContainerColor = Mist,
                    disabledContentColor = OnSurfaceVariant
                )
            ) {
                Text(stringResource(R.string.report_send_button), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
            }

            Text(
                "Note: Reports are saved locally in this phase. Actual government transmission is not implemented.",
                style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
            )
        }
    }
    }
}
