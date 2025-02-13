package org.example.weather

import UI.AppState
import UI.WeatherViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.icerock.moko.geo.compose.BindLocationTrackerEffect
import dev.icerock.moko.geo.compose.LocationTrackerAccuracy
import dev.icerock.moko.geo.compose.rememberLocationTrackerFactory
import dev.icerock.moko.permissions.PermissionState
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import weather.composeapp.generated.resources.Res
import weather.composeapp.generated.resources.air
import weather.composeapp.generated.resources.humidity
import weather.composeapp.generated.resources.weather
import kotlin.math.roundToInt

@Composable
@Preview
fun App() {
    MaterialTheme {
        val factory = rememberLocationTrackerFactory(LocationTrackerAccuracy.Best)
        val locationTracker = remember { factory.createLocationTracker() }
        val viewModel = viewModel { WeatherViewModel(locationTracker) }
        val citytosearch = remember { mutableStateOf("") }
        BindLocationTrackerEffect(locationTracker)
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val permissionState = viewModel.permissionState.collectAsState()
            val appState = viewModel.appState.collectAsState()
            when (permissionState.value) {
                PermissionState.Granted -> {
                    LaunchedEffect(key1 = Unit) {
                        viewModel.updateLocationData()
                    }
                    when (appState.value) {
                        is AppState.Error -> {
                            val message = (appState.value as AppState.Error).message
                            Text(message)
                        }

                        AppState.Loading -> {
                            CircularProgressIndicator()
                            Text("Loading...")
                        }

                        is AppState.Success -> {
                            val data = (appState.value as AppState.Success).data
                            Box(
                                modifier = Modifier.fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            colors = listOf(
                                                Color(0xff6d4db8),
                                                Color(0xff5610ff)
                                            )
                                        )
                                    ).systemBarsPadding()
                            )
                            {
                                Row(
                                    modifier = Modifier.fillMaxWidth().align(Alignment.TopStart)
                                        .padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        data.name, style = TextStyle(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Icon(
                                        imageVector = Icons.Filled.Notifications,
                                        contentDescription = "Notification",
                                        tint = Color.White,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.fillMaxWidth().align(Alignment.Center)
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center

                                    ) {
                                        OutlinedTextField(
                                            value = citytosearch.value,
                                            onValueChange = {
                                                citytosearch.value = it
                                            },
                                            label = {
                                                Text("Search City", color = Color.White)
                                            },
                                            singleLine = true,
                                            shape = RoundedCornerShape(20.dp),
                                            trailingIcon = {
                                                IconButton(onClick = {
                                                    viewModel.searchByCity(citytosearch.value)
                                                    citytosearch.value = ""

                                                }) {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Search,
                                                        contentDescription = "Search",
                                                        tint = Color.White,
                                                        modifier = Modifier.size(24.dp)
                                                    )
                                                }
                                            },
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = Color.White,
                                                unfocusedBorderColor = Color.White,
                                                textColor = Color.White,
                                                cursorColor = Color.White

                                            )
                                        )
                                    }
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Image(
                                        painter = painterResource(getImage(data.weather[0].main)),
                                        contentDescription = null,
                                        modifier = Modifier.size(120.dp),
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(
                                        text = data.name,
                                        style = MaterialTheme.typography.h6.copy(
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Spacer(modifier = Modifier.size(16.dp))
                                    Column(
                                        modifier = Modifier.fillMaxWidth().padding(16.dp).clip(
                                            RoundedCornerShape(16.dp)
                                        ).background(
                                            color = Color.White.copy(alpha = .1f)
                                        ).padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            (data.main.temp - 273.15).roundToInt()
                                                .toString() + "°C",
                                            style = MaterialTheme.typography.h3.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            ),
                                        )
                                        Text(
                                            data.weather[0].main,
                                            style = MaterialTheme.typography.h3.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.ExtraBold
                                            ),
                                            fontSize = 60.sp
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))
                                        Text(
                                            data.weather.getOrNull(0)?.description ?: "",
                                            style = MaterialTheme.typography.h6.copy(
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            ),
                                        )
                                        Spacer(modifier = Modifier.size(16.dp))

                                        WeatherCard(
                                            image = Res.drawable.humidity,
                                            title = "Humidity :",
                                            value = "${data.main.humidity}%"
                                        )
                                        WeatherCard(
                                            image = Res.drawable.air,
                                            title = "Wind :",
                                            value = "${data.wind.speed} m/s"
                                        )


                                    }
                                }

                            }
                        }
                    }
                }

                PermissionState.DeniedAlways -> {
                    Button(onClick = {
                        locationTracker.permissionsController.openAppSettings()
                    }) {
                        Text("Open settings")
                    }
                }

                else -> {
                    Button(onClick = {
                        viewModel.provideLocationPermission()
                    }) {
                        Text("Provide permission")
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(image: DrawableResource, title: String, value: String) {
    Row(
        modifier = Modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = title, color = Color.White
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = value, color = Color.White
        )
    }
}

fun getImage(main: String): DrawableResource {
    return Res.drawable.weather
}

