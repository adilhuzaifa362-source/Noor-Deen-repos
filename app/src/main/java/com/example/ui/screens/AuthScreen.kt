package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.JuristicMethod
import com.example.service.LocationHelper
import com.example.ui.components.AddCustomCityDialog
import com.example.viewmodel.NoorDeenViewModel
import java.util.Locale

@Composable
fun AuthScreen(
    viewModel: NoorDeenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val userProfile by viewModel.userProfile.collectAsState()
    val juristicMethod by viewModel.juristicMethod.collectAsState()

    var selectedTab by remember { mutableIntStateOf(if (userProfile.isLoggedIn) 2 else 0) } // 0: Sign In, 1: Sign Up, 2: Profile
    var emailInput by remember { mutableStateOf("") }
    var passwordInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Sign Up Fields
    var signupName by remember { mutableStateOf("") }
    var signupEmail by remember { mutableStateOf("") }
    var signupPassword by remember { mutableStateOf("") }
    var signupCity by remember { mutableStateOf("Makkah") }
    var signupCountry by remember { mutableStateOf("Saudi Arabia") }
    var signupLat by remember { mutableStateOf("21.4225") }
    var signupLon by remember { mutableStateOf("39.8262") }

    var feedbackMessage by remember { mutableStateOf<String?>(null) }
    var showAddCustomCityDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("auth_screen_view")
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Back Navigation Bar
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = { viewModel.navigateBack() },
                    modifier = Modifier.testTag("auth_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = if (userProfile.isLoggedIn) "User Account & Location" else "Account & City Setup",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Status Card / Hero Header
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Account",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = if (userProfile.isLoggedIn) userProfile.name else "Noor Deen Member",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (userProfile.isLoggedIn) "${userProfile.email} • ${userProfile.authProvider}" else "Sign in or create an account with custom coordinates",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        if (userProfile.isLoggedIn) {
            // Already Logged In Profile Overview
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "Account Profile & Prayer Timing Configuration",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        ProfileInfoRow(label = "Full Name", value = userProfile.name)
                        ProfileInfoRow(label = "Email Address", value = userProfile.email)
                        ProfileInfoRow(label = "Authentication Method", value = userProfile.authProvider)
                        ProfileInfoRow(label = "City & Country", value = "${userProfile.city}, ${userProfile.country}")
                        ProfileInfoRow(label = "Coordinates", value = "${String.format(Locale.US, "%.4f", userProfile.latitude)}° N, ${String.format(Locale.US, "%.4f", userProfile.longitude)}° E")

                        // Default Hanafi Calculation Badge
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Hanafi",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Default Timing: Hanafi (Shadow 2x)",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                    Text(
                                        text = "Our API automatically computes Asr and prayer times according to the Hanafi school for your city.",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showAddCustomCityDialog = true },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.LocationCity, contentDescription = "Edit City", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Edit Coordinates", fontSize = 12.sp)
                            }

                            Button(
                                onClick = { viewModel.logout() },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.weight(1f).testTag("auth_logout_button")
                            ) {
                                Text("Sign Out", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else {
            // Tab Selector between Sign In and Sign Up
            item {
                TabRow(
                    selectedTabIndex = selectedTab
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0; feedbackMessage = null },
                        text = { Text("Sign In", fontWeight = FontWeight.SemiBold) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1; feedbackMessage = null },
                        text = { Text("Create Account", fontWeight = FontWeight.SemiBold) }
                    )
                }
            }

            if (selectedTab == 0) {
                // SIGN IN TAB
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Sign In to Your Account",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            OutlinedTextField(
                                value = emailInput,
                                onValueChange = { emailInput = it; feedbackMessage = null },
                                label = { Text("Email Address") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_signin_email_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = passwordInput,
                                onValueChange = { passwordInput = it; feedbackMessage = null },
                                label = { Text("Password") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                                trailingIcon = {
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(
                                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                            contentDescription = "Toggle password"
                                        )
                                    }
                                },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_signin_password_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            if (feedbackMessage != null) {
                                Text(
                                    text = feedbackMessage ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Button(
                                onClick = {
                                    if (emailInput.isBlank() || passwordInput.isBlank()) {
                                        feedbackMessage = "Please enter both email and password."
                                    } else {
                                        viewModel.login(emailInput, passwordInput)
                                        viewModel.navigateBack()
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_signin_submit_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Sign In")
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            // Google Sign In Button
                            OutlinedButton(
                                onClick = {
                                    viewModel.loginWithGoogle()
                                    viewModel.navigateBack()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_signin_google_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Continue with Google")
                            }

                            // Guest Mode Option
                            TextButton(
                                onClick = {
                                    viewModel.logout()
                                    viewModel.navigateBack()
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Continue as Guest (No Sign-In Needed)")
                            }
                        }
                    }
                }
            } else {
                // CREATE ACCOUNT TAB (With Latitude & Longitude and Hanafi timing by default)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(
                                text = "Create Account & Custom City",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Enter your profile info and custom coordinates. Our API will calculate your daily prayer timings defaulting to Hanafi jurisprudence.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            OutlinedTextField(
                                value = signupName,
                                onValueChange = { signupName = it },
                                label = { Text("Full Name *") },
                                leadingIcon = { Icon(Icons.Default.Person, contentDescription = "Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_signup_name_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = signupEmail,
                                onValueChange = { signupEmail = it },
                                label = { Text("Email Address *") },
                                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_signup_email_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            OutlinedTextField(
                                value = signupPassword,
                                onValueChange = { signupPassword = it },
                                label = { Text("Password *") },
                                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = "Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("auth_signup_password_input"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            // Quick GPS Auto-Detect Button
                            OutlinedButton(
                                onClick = {
                                    LocationHelper.getUserCurrentLocation(
                                        context = context,
                                        onLocationReceived = { lat, lon ->
                                            signupLat = String.format(Locale.US, "%.4f", lat)
                                            signupLon = String.format(Locale.US, "%.4f", lon)
                                            if (signupCity.isBlank() || signupCity == "Makkah") signupCity = "My City"
                                        },
                                        onError = { feedbackMessage = "GPS unavailable: $it" }
                                    )
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_signup_gps_button")
                            ) {
                                Icon(Icons.Default.MyLocation, contentDescription = "GPS", modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Auto-Detect My GPS Coordinates")
                            }

                            // City & Country
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = signupCity,
                                    onValueChange = { signupCity = it },
                                    label = { Text("City *") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("auth_signup_city_input"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = signupCountry,
                                    onValueChange = { signupCountry = it },
                                    label = { Text("Country") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("auth_signup_country_input"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            // Latitude and Longitude Fields
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    value = signupLat,
                                    onValueChange = { signupLat = it },
                                    label = { Text("Latitude *") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("auth_signup_lat_input"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                                OutlinedTextField(
                                    value = signupLon,
                                    onValueChange = { signupLon = it },
                                    label = { Text("Longitude *") },
                                    singleLine = true,
                                    modifier = Modifier.weight(1f).testTag("auth_signup_lon_input"),
                                    shape = RoundedCornerShape(10.dp)
                                )
                            }

                            // Highlighting Default Hanafi Timing Notice
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Hanafi Timing Default",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Calculation Method: Hanafi Timing (Default)",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            if (feedbackMessage != null) {
                                Text(
                                    text = feedbackMessage ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

                            Button(
                                onClick = {
                                    if (signupName.isBlank() || signupEmail.isBlank() || signupPassword.isBlank()) {
                                        feedbackMessage = "Please fill in all required fields."
                                        return@Button
                                    }
                                    val lat = signupLat.toDoubleOrNull()
                                    val lon = signupLon.toDoubleOrNull()
                                    if (lat == null || lon == null) {
                                        feedbackMessage = "Please enter valid numerical latitude and longitude."
                                        return@Button
                                    }

                                    viewModel.signup(
                                        name = signupName,
                                        email = signupEmail,
                                        pass = signupPassword,
                                        city = signupCity,
                                        country = signupCountry,
                                        lat = lat,
                                        lon = lon
                                    )
                                    viewModel.navigateBack()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("auth_signup_submit_button"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Create Account & Calculate Timing")
                            }
                        }
                    }
                }
            }
        }
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
private fun ProfileInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
