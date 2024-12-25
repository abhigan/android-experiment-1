package com.example.android_experiment_1

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.android_experiment_1.ui.theme.AndroidExperiment1Theme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import androidx.compose.runtime.getValue
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority

private const val logTag = "LOGTAG1"
private val locationProviderTxt = mutableStateOf("?")
private val altitudeTxt = mutableStateOf("?")
private val speedTxt = mutableStateOf("?")
private val longitudeTxt = mutableStateOf("?")
private val latitudeTxt = mutableStateOf("?")

class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            AndroidExperiment1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        requestPermissions()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if(locationResult.locations.size != 1) {
                    Log.e(logTag, "locationResult.locations.size: ${locationResult.locations.size}");
                }
                val location = locationResult.locations[0];
                locationProviderTxt.value = location.provider ?: "!"
                altitudeTxt.value = location.altitude.toString()
                speedTxt.value = location.speed.toString()
                longitudeTxt.value = location.longitude.toString()
                latitudeTxt.value = location.latitude.toString()
            }
        }

        val locationRequest = LocationRequest.Builder(5000)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        fusedLocationClient.requestLocationUpdates(locationRequest,
            locationCallback,
            Looper.getMainLooper())
    }

    private fun requestPermissions() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                }
                else -> {
                    // No location access granted.
                }
            }
        }

        // Before you perform the actual permission request, check whether your app
        // already has the permissions, and whether your app needs to show a permission
        // rationale dialog. For more details, see Request permissions:
        // https://developer.android.com/training/permissions/requesting#request-permission
        locationPermissionRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FlowRow (
            horizontalArrangement = Arrangement.spacedBy(40.dp)
        ) {
            val spacing = 5.dp;
            
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                val txt by latitudeTxt;
                Text(
                    text = "Lat",
                    modifier = modifier,
                )
                Text(
                    text = txt,
                    modifier = modifier,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                val txt by longitudeTxt;
                Text(
                    text = "Long",
                    modifier = modifier,
                )
                Text(
                    text = txt,
                    modifier = modifier,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                val txt by speedTxt;
                Text(
                    text = "Speed",
                    modifier = modifier,
                )
                Text(
                    text = txt,
                    modifier = modifier,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                val txt by altitudeTxt;
                Text(
                    text = "Alt",
                    modifier = modifier,
                )
                Text(
                    text = txt,
                    modifier = modifier,
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(spacing)) {
                val txt by locationProviderTxt;
                Text(
                    text = "Provider",
                    modifier = modifier,
                )
                Text(
                    text = txt,
                    modifier = modifier,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidExperiment1Theme {
        Scaffold { _ ->
            Greeting("Android")
        }
    }
}