package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CalculationMethod
import com.example.model.JuristicMethod
import com.example.service.LocationHelper
import java.util.Locale

@Composable
fun AddCustomCityDialog(
    onDismiss: () -> Unit,
    onAddCity: (name: String, country: String, lat: Double, lon: Double, calcMethod: CalculationMethod, juristicMethod: JuristicMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var cityName by remember { mutableStateOf("") }
    var countryName by remember { mutableStateOf("") }
    var latText by remember { mutableStateOf("") }
    var lonText by remember { mutableStateOf("") }
    var selectedCalcMethod by remember { mutableStateOf(CalculationMethod.MWL) }
    var selectedJuristicMethod by remember { mutableStateOf(JuristicMethod.HANAFI) } // Defaults to Hanafi per user request
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isDetectingLocation by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier.testTag("add_custom_city_dialog"),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationCity,
                        contentDescription = "Custom City",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Add Custom Coordinates",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Defaulting to Hanafi Calculation",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Specify city name, latitude, and longitude. Our API will calculate daily prayer times with Hanafi Asr (Shadow 2x).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Quick Detect GPS Button
                OutlinedButton(
                    onClick = {
                        isDetectingLocation = true
                        LocationHelper.getUserCurrentLocation(
                            context = context,
                            onLocationReceived = { lat, lon ->
                                latText = String.format(Locale.US, "%.4f", lat)
                                lonText = String.format(Locale.US, "%.4f", lon)
                                if (cityName.isBlank()) cityName = "My Current Location"
                                if (countryName.isBlank()) countryName = "Local Device GPS"
                                isDetectingLocation = false
                            },
                            onError = {
                                errorMessage = "GPS detection unavailable: $it"
                                isDetectingLocation = false
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("detect_gps_coordinates_button")
                ) {
                    Icon(Icons.Default.MyLocation, contentDescription = "GPS", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isDetectingLocation) "Detecting GPS..." else "Auto-Fill My GPS Coordinates")
                }

                // City & Country Inputs
                OutlinedTextField(
                    value = cityName,
                    onValueChange = { cityName = it; errorMessage = null },
                    label = { Text("City Name *") },
                    placeholder = { Text("e.g. Chicago, Birmingham, Lahore") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_city_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = countryName,
                    onValueChange = { countryName = it },
                    label = { Text("Country") },
                    placeholder = { Text("e.g. United States, UK, Pakistan") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("custom_country_name_input"),
                    shape = RoundedCornerShape(10.dp)
                )

                // Latitude & Longitude Inputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = latText,
                        onValueChange = { latText = it; errorMessage = null },
                        label = { Text("Latitude *") },
                        placeholder = { Text("e.g. 41.8781") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("custom_city_lat_input"),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = lonText,
                        onValueChange = { lonText = it; errorMessage = null },
                        label = { Text("Longitude *") },
                        placeholder = { Text("e.g. -87.6298") },
                        singleLine = true,
                        modifier = Modifier.weight(1f).testTag("custom_city_lon_input"),
                        shape = RoundedCornerShape(10.dp)
                    )
                }

                // Juristic Method Info Card (Highlighting Hanafi as Default)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Hanafi Default",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Juristic Method: Hanafi (Default)",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Asr time begins when shadow equals twice object length (Shadow 2x).",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (cityName.isBlank()) {
                        errorMessage = "Please enter a city name."
                        return@Button
                    }
                    val lat = latText.toDoubleOrNull()
                    val lon = lonText.toDoubleOrNull()
                    if (lat == null || lat !in -90.0..90.0) {
                        errorMessage = "Valid latitude required between -90 and 90."
                        return@Button
                    }
                    if (lon == null || lon !in -180.0..180.0) {
                        errorMessage = "Valid longitude required between -180 and 180."
                        return@Button
                    }

                    onAddCity(
                        cityName,
                        if (countryName.isBlank()) "Custom" else countryName,
                        lat,
                        lon,
                        selectedCalcMethod,
                        selectedJuristicMethod
                    )
                    onDismiss()
                },
                modifier = Modifier.testTag("save_custom_city_button")
            ) {
                Text("Save & Calculate")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.testTag("cancel_custom_city_button")
            ) {
                Text("Cancel")
            }
        }
    )
}
