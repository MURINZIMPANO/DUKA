package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.duka.app.data.local.entity.Promo
import com.duka.app.data.repository.BusinessRepository
import com.duka.app.data.repository.PromoRepository
import com.duka.app.ui.components.DecorativeBackdrop
import com.duka.app.ui.components.DukaPage
import com.duka.app.ui.theme.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PromoScreen(
    businessRepository: BusinessRepository,
    promoRepository: PromoRepository,
    onBack: (() -> Unit)? = null
) {
    val business by businessRepository.getActiveBusiness().collectAsState(initial = null)
    val scope = rememberCoroutineScope()
    var title by remember { mutableStateOf("") }
    var discountPercent by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }

    val businessId = business?.id ?: 0L
    val livePromos by promoRepository.getLivePromos(businessId).collectAsState(initial = emptyList())
    val allPromos by promoRepository.getPromosByBusiness(businessId).collectAsState(initial = emptyList())

    val isValid = title.isNotBlank() && discountPercent.isNotBlank() &&
            startDate.isNotBlank() && endDate.isNotBlank()

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.US) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
    ) {
        DecorativeBackdrop(
            page = DukaPage.PROMO,
            modifier = Modifier.fillMaxSize()
        )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Promotions", color = White) },
                navigationIcon = {
                    onBack?.let { back ->
                        IconButton(onClick = back) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = White)
                        }
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
            // Create promo form
            item {
                Text(
                    "Create a promotion",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Ink
                    )
                )

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Promo title") },
                    placeholder = { Text("e.g. Weekend Sale") },
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

                OutlinedTextField(
                    value = discountPercent,
                    onValueChange = { discountPercent = it.filter { c -> c.isDigit() } },
                    label = { Text("Discount %") },
                    placeholder = { Text("e.g. 15") },
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

                OutlinedTextField(
                    value = startDate,
                    onValueChange = { startDate = it },
                    label = { Text("Start date (YYYY-MM-DD)") },
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

                OutlinedTextField(
                    value = endDate,
                    onValueChange = { endDate = it },
                    label = { Text("End date (YYYY-MM-DD)") },
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

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        scope.launch {
                            val startMs = try { dateFormat.parse(startDate)?.time ?: System.currentTimeMillis() } catch (e: Exception) { System.currentTimeMillis() }
                            val endMs = try { dateFormat.parse(endDate)?.time ?: System.currentTimeMillis() } catch (e: Exception) { System.currentTimeMillis() }

                            promoRepository.createPromo(
                                Promo(
                                    businessId = businessId,
                                    title = title.trim(),
                                    discountPercent = discountPercent.toIntOrNull() ?: 0,
                                    startDate = startMs,
                                    endDate = endMs,
                                    isLive = true
                                )
                            )
                            title = ""
                            discountPercent = ""
                            startDate = ""
                            endDate = ""
                            showSuccess = true
                        }
                    },
                    enabled = isValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Forest,
                        contentColor = White,
                        disabledContainerColor = Mist,
                        disabledContentColor = OnSurfaceVariant
                    )
                ) {
                    Text("Publish promo", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                }

                if (showSuccess) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Forest.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "✓ Promo published",
                            modifier = Modifier.padding(12.dp),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Forest)
                        )
                    }
                }
            }

            // Live promos section
            if (livePromos.isNotEmpty()) {
                item {
                    Text(
                        "Live Promos",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(livePromos) { promo ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    promo.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    "${promo.discountPercent}% off",
                                    style = MaterialTheme.typography.bodySmall.copy(color = Forest)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Forest.copy(alpha = 0.1f)
                            ) {
                                Text(
                                    "Live",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(color = Forest)
                                )
                            }
                        }
                    }
                }
            }

            // All promos section
            if (allPromos.isNotEmpty()) {
                item {
                    Text(
                        "All Promos",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = Ink
                        ),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                items(allPromos.filter { !it.isLive || livePromos.none { lp -> lp.id == it.id } }) { promo ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = White)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    promo.title,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                )
                                Text(
                                    "${promo.discountPercent}% off",
                                    style = MaterialTheme.typography.bodySmall.copy(color = OnSurfaceVariant)
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (promo.isLive) Forest.copy(alpha = 0.1f) else Mist
                            ) {
                                Text(
                                    if (promo.isLive) "Live" else "Ended",
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (promo.isLive) Forest else OnSurfaceVariant
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
