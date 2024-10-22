package com.techlambda.camerainstallation.dev

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.techlambda.authlibrary.ui.AuthNavHost
import com.techlambda.authlibrary.ui.data.AuthPrefManager
import com.techlambda.camerainstallation.dev.ui.theme.PushNotificationLibraryTheme
import com.techlambda.pushnotificationlibrary.PushNotificationInitializer
import com.techlambda.pushnotificationlibrary.permissionComposable.NotificationPermissionRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PushNotificationLibraryTheme {
                val navController = rememberNavController()
                var showHomeScreen by remember { mutableStateOf(false) }
                val prefManager = AuthPrefManager(LocalContext.current)
                CompositionLocalProvider(value = LocalNavigationProvider provides navController) {
                    Surface(color = MaterialTheme.colorScheme.background) {
                        if(showHomeScreen) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                NotificationPermissionRequest(permissionGranted = {
                                }) {
                                    Toast.makeText(
                                        this,
                                        "Permission denied. Please allow from settings to get latest updates.",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                PushNotificationInitializer.initialize(this, userId = prefManager.getUserData()?.userId!!)
                            }else {
                                PushNotificationInitializer.initialize(this, userId = prefManager.getUserData()?.userId!!)
                            }
                            AppNavHost(Modifier)
                        }else {
                            AuthNavHost(
                                modifier = Modifier,
                                appLogo = {
                                    AppLogo()
                                },
                                onCodeSuccess = {
                                    showHomeScreen = true
                                },
                                navHostController = navController,
                                onSignInSuccess = {
                                    showHomeScreen = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PushNotificationLibraryTheme {
        Greeting("Android")
    }
}