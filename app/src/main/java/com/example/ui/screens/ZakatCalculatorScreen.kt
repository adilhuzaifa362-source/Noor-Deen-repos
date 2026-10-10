package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.NoorDeenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ZakatCalculatorScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val cashInput by viewModel.cashOnHand.collectAsState()
    val bankInput by viewModel.bankSavings.collectAsState()
    val goldGramsInput by viewModel.goldGrams.collectAsState()
    val goldPriceInput by viewModel.goldPricePerGram.collectAsState()
    val silverGramsInput by viewModel.silverGrams.collectAsState()
    val silverPriceInput by viewModel.silverPricePerGram.collectAsState()
    val merchandiseInput by viewModel.businessMerchandise.collectAsState()
    val liabilitiesInput by viewModel.shortTermLiabilities.collectAsState()
    val currency by viewModel.zakatCurrency.collectAsState()

    var currencyDropdownExpanded by remember { mutableStateOf(false) }

    // Parse numeric values
    val cash = cashInput.toDoubleOrNull() ?: 0.0
    val bank = bankInput.toDoubleOrNull() ?: 0.0
    val goldGrams = goldGramsInput.toDoubleOrNull() ?: 0.0
    val goldPrice = goldPriceInput.toDoubleOrNull() ?: 0.0
    val silverGrams = silverGramsInput.toDoubleOrNull() ?: 0.0
    val silverPrice = silverPriceInput.toDoubleOrNull() ?: 0.0
    val merchandise = merchandiseInput.toDoubleOrNull() ?: 0.0
    val liabilities = liabilitiesInput.toDoubleOrNull() ?: 0.0

    // Value calculations
    val goldTotal = goldGrams * goldPrice
    val silverTotal = silverGrams * silverPrice
    val grossWealth = cash + bank + goldTotal + silverTotal + merchandise
    val netWealth = (grossWealth - liabilities).coerceAtLeast(0.0)

    // Silver Nisab is standard for liquid currency: 612.36g
    val silverNisabValue = 612.36 * silverPrice
    // Gold Nisab: 87.48g
    val goldNisabValue = 87.48 * goldPrice

    val isZakatWajib = netWealth >= silverNisabValue && netWealth > 0
    val zakatPayable = if (isZakatWajib) netWealth * 0.025 else 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("zakat_calculator_screen")
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("zakat_back_button")
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Zakat Calculator (حاسبة الزكاة)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Multi-Asset Evaluation based on Nisab Thresholds",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 80.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Result Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isZakatWajib)
                            MaterialTheme.colorScheme.primary
                        else
                            MaterialTheme.colorScheme.surfaceVariant
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = if (isZakatWajib) "Zakat is Mandatory (Wajib)" else "Below Nisab Threshold",
                                style = MaterialTheme.typography.labelLarge,
                                color = if (isZakatWajib) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = FontWeight.Bold
                            )

                            // Currency Selector
                            ExposedDropdownMenuBox(
                                expanded = currencyDropdownExpanded,
                                onExpandedChange = { currencyDropdownExpanded = it }
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White.copy(alpha = 0.25f),
                                    modifier = Modifier.menuAnchor()
                                ) {
                                    Text(
                                        text = "$currency ▼",
                                        fontWeight = FontWeight.Bold,
                                        color = if (isZakatWajib) Color.White else MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    )
                                }
                                ExposedDropdownMenu(
                                    expanded = currencyDropdownExpanded,
                                    onDismissRequest = { currencyDropdownExpanded = false }
                                ) {
                                    listOf("USD", "SAR", "AED", "PKR", "INR", "GBP", "EUR").forEach { c ->
                                        DropdownMenuItem(
                                            text = { Text(c) },
                                            onClick = {
                                                viewModel.zakatCurrency.value = c
                                                currencyDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = String.format("%.2f %s", zakatPayable, currency),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isZakatWajib) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "2.5% of Eligible Net Wealth (${String.format("%.2f", netWealth)} $currency)",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (isZakatWajib) Color.White.copy(alpha = 0.85f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "Silver Nisab (612.36g): ${String.format("%.2f", silverNisabValue)} $currency • Gold Nisab (87.48g): ${String.format("%.2f", goldNisabValue)} $currency",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            color = if (isZakatWajib) Color.White.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Input Fields Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Assets & Wealth Owned (Hawl: 1 Lunar Year)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Cash
                        OutlinedTextField(
                            value = cashInput,
                            onValueChange = { viewModel.cashOnHand.value = it },
                            label = { Text("Cash on Hand ($currency)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("zakat_cash_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Bank Savings
                        OutlinedTextField(
                            value = bankInput,
                            onValueChange = { viewModel.bankSavings.value = it },
                            label = { Text("Bank Accounts & Deposits ($currency)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("zakat_bank_input")
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Gold
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = goldGramsInput,
                                onValueChange = { viewModel.goldGrams.value = it },
                                label = { Text("Gold (Grams)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("zakat_gold_grams_input")
                            )
                            OutlinedTextField(
                                value = goldPriceInput,
                                onValueChange = { viewModel.goldPricePerGram.value = it },
                                label = { Text("Rate/Gram ($currency)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("zakat_gold_rate_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Silver
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = silverGramsInput,
                                onValueChange = { viewModel.silverGrams.value = it },
                                label = { Text("Silver (Grams)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("zakat_silver_grams_input")
                            )
                            OutlinedTextField(
                                value = silverPriceInput,
                                onValueChange = { viewModel.silverPricePerGram.value = it },
                                label = { Text("Rate/Gram ($currency)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("zakat_silver_rate_input")
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Business Stock
                        OutlinedTextField(
                            value = merchandiseInput,
                            onValueChange = { viewModel.businessMerchandise.value = it },
                            label = { Text("Trade Goods & Merchandise ($currency)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("zakat_merchandise_input")
                        )

                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Deductible Liabilities & Debts Due Now",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = liabilitiesInput,
                            onValueChange = { viewModel.shortTermLiabilities.value = it },
                            label = { Text("Short-Term Debts & Due Bills ($currency)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("zakat_liabilities_input")
                        )
                    }
                }
            }
        }
    }
}
