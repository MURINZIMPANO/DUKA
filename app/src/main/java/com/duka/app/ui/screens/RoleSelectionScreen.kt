package com.duka.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.duka.app.ui.theme.Canvas
import com.duka.app.ui.theme.Forest
import com.duka.app.ui.theme.Ink
import com.duka.app.ui.theme.Mist
import com.duka.app.ui.theme.OnSurfaceVariant
import com.duka.app.ui.theme.White

@Composable
fun RoleSelectionScreen(
    businessName: String,
    onSelectRole: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Canvas)
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Duka",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = Forest
                )
            )

            Text(
                text = businessName,
                style = MaterialTheme.typography.titleLarge,
                color = Ink
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Who is using the app today?",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Role cards in horizontal row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Owner card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .border(1.dp, Mist, RoundedCornerShape(12.dp))
                        .clickable { onSelectRole("owner") }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Storefront,
                            contentDescription = "Business Owner",
                            modifier = Modifier.size(32.dp),
                            tint = Forest
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Business\nOwner",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Forest,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Employee card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .border(1.dp, Mist, RoundedCornerShape(12.dp))
                        .clickable { onSelectRole("employee") }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Badge,
                            contentDescription = "Employee",
                            modifier = Modifier.size(32.dp),
                            tint = Forest
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Employee",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Forest,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Shopper / Client card
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(White)
                        .border(1.dp, Mist, RoundedCornerShape(12.dp))
                        .clickable { onSelectRole("client") }
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ShoppingBag,
                            contentDescription = "Shopper / Client",
                            modifier = Modifier.size(32.dp),
                            tint = Forest
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Shopper\n/ Client",
                            style = MaterialTheme.typography.labelMedium.copy(
                                color = Forest,
                                fontWeight = FontWeight.Bold
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
