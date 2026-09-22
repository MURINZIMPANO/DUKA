package com.duka.phase3.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.data.local.entity.Product
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.phase3.data.CategoryTemplates
import kotlinx.coroutines.launch

/**
 * Phase 3 — Category starter templates: the offer dialog.
 * Shown when the shop's category has a starter list and the Products screen is
 * empty (or right after registration in future work). Accepting inserts the
 * seeded products with placeholder pricing the owner can then edit, remove,
 * or add to. Declining inserts nothing — this is a time-saver, not a requirement.
 */
@Composable
fun CategoryQuickStartDialog(
    category: String,
    businessId: Long,
    productRepository: com.duka.app.data.repository.ProductRepository,
    onDismiss: () -> Unit
) {
    if (!CategoryTemplates.hasTemplate(category)) {
        // Unknown category: nothing to offer — call sites can skip showing the dialog.
        androidx.compose.runtime.LaunchedEffect(Unit) { onDismiss() }
        return
    }

    val starters = remember(category) { CategoryTemplates.starterProducts(category) }
    // Every item pre-selected; the owner can uncheck anything they don't want.
    val selected = remember(category) { mutableStateOf(starters.map { true }.toBooleanArray()) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                CategoryTemplates.offerTitle(category),
                fontSize = 17.sp,
                color = Ink
            )
        },
        text = {
            Column {
                Text(
                    CategoryTemplates.offerBody(category),
                    fontSize = 13.sp,
                    color = OnSurfaceVariant
                )
                Spacer(Modifier.height(10.dp))
                LazyColumn(modifier = Modifier.height(260.dp)) {
                    items(starters.size) { index ->
                        val item = starters[index]
                        androidx.compose.foundation.layout.Row(
                            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = selected.value[index],
                                onCheckedChange = { checked -> selected.value[index] = checked }
                            )
                            Text(item.name, fontSize = 14.sp, color = Ink)
                            Spacer(Modifier.padding(horizontal = 4.dp))
                            Text(
                                "₣%,.0f".format(item.defaultPriceRwf),
                                fontSize = 12.sp,
                                color = OnSurfaceVariant
                            )
                        }
                    }
                }
                Text(
                    "Prices are placeholder defaults — edit them after adding.",
                    fontSize = 11.sp,
                    color = OnSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val chosen = starters.filterIndexed { i, _ -> selected.value[i] }
                scope.launch {
                    chosen.forEach { starter ->
                        productRepository.addProduct(
                            Product(
                                businessId = businessId,
                                name = starter.name,
                                price = starter.defaultPriceRwf,
                                stockQuantity = starter.defaultStock,
                                category = starter.category
                            )
                        )
                    }
                    onDismiss()
                }
            }) { Text("Add selected") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("No thanks") }
        }
    )
}
