package com.example.ui.screens

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CompassCalibration
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Mosque
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import com.example.ui.components.AddCustomCityDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.IslamicData
import com.example.model.AppLanguage
import com.example.model.CalculationMethod
import com.example.model.CityLocation
import com.example.model.JuristicMethod
import com.example.model.PrayerTime
import com.example.service.LocationHelper
import com.example.viewmodel.NoorDeenViewModel
import com.example.viewmodel.SubScreen

@Composable
fun PrayerScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val currentSubScreen by viewModel.currentSubScreen.collectAsState()

    when (currentSubScreen) {
        SubScreen.QIBLA_COMPASS -> QiblaScreen(viewModel = viewModel, modifier = modifier)
        SubScreen.MASJID_NETWORK -> MasjidNetworkScreen(viewModel = viewModel, modifier = modifier)
        else -> PrayerTimesMainView(viewModel = viewModel, modifier = modifier)
    }
}

@Composable
fun PrayerTimesMainView(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCity by viewModel.selectedCity.collectAsState()
    val calculationMethod by viewModel.calculationMethod.collectAsState()
    val juristicMethod by viewModel.juristicMethod.collectAsState()
    val prayerTimes by viewModel.prayerTimes.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()

    var showCityDialog by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }
    var showAddCustomCityDialog by remember { mutableStateOf(false) }
    var selectedCountryFilter by remember { mutableStateOf<String?>(null) }
    var citySearchQuery by remember { mutableStateOf("") }

    val nextPrayer = prayerTimes.firstOrNull { it.isNext } ?: prayerTimes.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("prayer_screen_main_view")
    ) {
        // Hero Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.primaryContainer
                            )
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // City Pill
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White.copy(alpha = 0.2f))
                                .clickable { showCityDialog = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                                .testTag("city_selector_pill")
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = MaterialTheme.colorScheme.secondary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${selectedCity.name}, ${selectedCity.country}",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Settings Icon
                        IconButton(
                            onClick = { showSettingsDialog = true },
                            modifier = Modifier.testTag("prayer_settings_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Tune,
                                contentDescription = "Calculation Settings",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (nextPrayer != null) {
                        Text(
                            text = "Next Prayer: ${nextPrayer.nameEn} (${nextPrayer.nameAr})",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = nextPrayer.timeFormatted,
                            fontSize = 44.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Based on astronomical solar coordinates • ${calculationMethod.displayName}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Action Quick Access Buttons (Qibla Compass & Masjid Network)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Hardware Qibla Compass Button
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.openSubScreen(SubScreen.QIBLA_COMPASS) }
                    .testTag("open_qibla_compass_button"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.secondary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Explore,
                            contentDescription = "Qibla Compass",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Qibla Compass",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        Text(
                            text = "Live Sensor Fusion",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Community Masjid Jamaat Synchronizer Button
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { viewModel.openSubScreen(SubScreen.MASJID_NETWORK) }
                    .testTag("open_masjid_network_button"),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mosque,
                            contentDescription = "Masjid Network",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Masjid Jamaat",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Iqamah Sync",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Daily Prayer Times List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(top = 8.dp, bottom = 80.dp)
        ) {
            items(prayerTimes) { pt ->
                PrayerTimeItemCard(
                    prayerTime = pt,
                    currentLanguage = currentLanguage
                )
            }
        }
    }

    // City Selection Dialog (Personalized for India & Neighbors)
    if (showCityDialog) {
        val filteredCities = IslamicData.worldCities.filter { city ->
            val matchesCountry = selectedCountryFilter == null || city.country.equals(selectedCountryFilter, ignoreCase = true)
            val matchesQuery = citySearchQuery.isBlank() || city.name.contains(citySearchQuery, ignoreCase = true) || city.country.contains(citySearchQuery, ignoreCase = true)
            matchesCountry && matchesQuery
        }

        AlertDialog(
            onDismissRequest = { showCityDialog = false },
            title = {
                Column {
                    Text("Select City (المدينة)", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Personalized for India & Neighboring Countries", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                }
            },
            text = {
                Column {
                    OutlinedButton(
                        onClick = {
                            showCityDialog = false
                            showAddCustomCityDialog = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .testTag("prayer_add_custom_city_button")
                    ) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Add Coordinates", modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Add Custom Coordinates (Lat/Long)")
                    }

                    // Regional Country Filter Chips
                    val regionalCountriesWithAll = listOf(
                        Pair(null, "All Regions"),
                        Pair("India", "India 🇮🇳"),
                        Pair("Pakistan", "Pakistan 🇵🇰"),
                        Pair("Bangladesh", "Bangladesh 🇧🇩"),
                        Pair("Afghanistan", "Afghanistan 🇦🇫"),
                        Pair("Nepal", "Nepal 🇳🇵"),
                        Pair("Sri Lanka", "Sri Lanka 🇱🇰"),
                        Pair("Maldives", "Maldives 🇲🇻"),
                        Pair("Bhutan", "Bhutan 🇧🇹"),
                        Pair("Myanmar", "Myanmar 🇲🇲")
                    )
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        items(regionalCountriesWithAll) { (countryCode, label) ->
                            val isSelected = selectedCountryFilter == countryCode
                            FilterChip(
                                selected = isSelected,
                                onClick = { selectedCountryFilter = countryCode },
                                label = { Text(label, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.primary
                                )
                            )
                        }
                    }

                    // City Search Bar
                    OutlinedTextField(
                        value = citySearchQuery,
                        onValueChange = { citySearchQuery = it },
                        placeholder = { Text("Search city (Delhi, Karachi, Dhaka...)", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", modifier = Modifier.size(16.dp)) },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        shape = RoundedCornerShape(8.dp)
                    )

                    LazyColumn(modifier = Modifier.height(240.dp)) {
                        items(filteredCities) { city ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        viewModel.setCity(city)
                                        showCityDialog = false
                                    }
                                    .padding(vertical = 6.dp, horizontal = 4.dp)
                            ) {
                                RadioButton(
                                    selected = city.name == selectedCity.name,
                                    onClick = {
                                        viewModel.setCity(city)
                                        showCityDialog = false
                                    }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = city.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Text(
                                        text = "${city.country} • Lat: ${city.latitude}, Lon: ${city.longitude}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showCityDialog = false }) { Text("Close") }
            }
        )
    }

    // Settings Dialog
    if (showSettingsDialog) {
        AlertDialog(
            onDismissRequest = { showSettingsDialog = false },
            title = { Text("Calculation Methods", style = MaterialTheme.typography.titleMedium) },
            text = {
                Column {
                    Text(
                        text = "Astronomical Algorithm:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CalculationMethod.entries.forEach { method ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setCalculationMethod(method) }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = method == calculationMethod,
                                onClick = { viewModel.setCalculationMethod(method) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = method.displayName, style = MaterialTheme.typography.bodySmall)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Asr Juristic Calculation:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    JuristicMethod.entries.forEach { juristic ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.setJuristicMethod(juristic) }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = juristic == juristicMethod,
                                onClick = { viewModel.setJuristicMethod(juristic) }
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = juristic.displayName, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showSettingsDialog = false }) { Text("Save") }
            }
        )
    }

    if (showAddCustomCityDialog) {
        AddCustomCityDialog(
            onDismiss = { showAddCustomCityDialog = false },
            onAddCity = { name, country, lat, lon, calcMethod, juristic ->
                viewModel.addCustomCity(name, country, lat, lon, calcMethod, juristic)
            }
        )
    }
}

@Composable
fun PrayerTimeItemCard(
    prayerTime: PrayerTime,
    currentLanguage: AppLanguage
) {
    val isNext = prayerTime.isNext

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("prayer_card_${prayerTime.nameEn.lowercase()}"),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isNext)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isNext) 4.dp else 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(
                            if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Prayer Notification",
                        tint = if (isNext) Color.White else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    val localName = when (currentLanguage) {
                        AppLanguage.URDU -> prayerTime.nameUr
                        AppLanguage.HINDI -> prayerTime.nameHi
                        AppLanguage.ARABIC -> prayerTime.nameAr
                        else -> prayerTime.nameEn
                    }
                    Text(
                        text = localName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isNext) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = prayerTime.nameAr,
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isNext) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(
                            text = "NEXT",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = prayerTime.timeFormatted,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isNext) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
