package com.techlambda.pushnotificationlibrary.permissionComposable

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun NotificationPermissionRequest(permissionGranted: () -> Unit, setDontAskAgain: ()-> Unit) {
    val context = LocalContext.current

    var showRationale by remember { mutableStateOf(false) }
    var showSettingsDialog by remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            permissionGranted()
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )
            ) {
                showRationale = true
            } else {
                showSettingsDialog = true
            }
        }
    }

    val requestNotificationPermission: () -> Unit = {
        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    if (showRationale) {
        RationaleDialog(onDismiss = { dontAskAgain ->
            showRationale = false
            if (dontAskAgain) {
                setDontAskAgain()
            }
        }, onConfirm = { dontAskAgain ->
            showRationale = false
            requestNotificationPermission()
            if (dontAskAgain) {
                setDontAskAgain()
            }
        })
    }

    if (showSettingsDialog) {
        SettingsDialog(onDismiss = { dontAskAgain ->
            showSettingsDialog = false
            if (dontAskAgain) {
                setDontAskAgain()
            }
        }, onConfirm = { dontAskAgain ->
            showSettingsDialog = false
            openAppSettings(context)
            if (dontAskAgain) {
                setDontAskAgain()
            }
        })
    }

    if(ActivityCompat.checkSelfPermission(context,
            Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
        LaunchedEffect(key1 = requestPermissionLauncher) {
            requestNotificationPermission()
        }
    }
}

@Composable
fun RationaleDialog(onDismiss: (dontAskAgain:Boolean) -> Unit, onConfirm: (dontAskAgain:Boolean) -> Unit) {
    val isChecked = remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { onDismiss(isChecked.value) },
        title = { Text("Notification Permission Required") },
        text = {
            Column {
                Text("This app requires the notification permission to notify you about important updates.")
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
                    Text(text = "Don't ask again", color = Color(0XFF05A8B3))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(isChecked.value) }) {
                Text("Allow")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(isChecked.value) }) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun SettingsDialog(onDismiss: (dontAskAgain:Boolean) -> Unit, onConfirm: (dontAskAgain:Boolean) -> Unit) {
    val isChecked = remember { mutableStateOf(false) }
    AlertDialog(
        onDismissRequest = { onDismiss(isChecked.value) },
        title = { Text("Notification Permission Denied") },
        text = {
            Column {
                Text("You have denied the notification permission. Please go to settings to enable it.")
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 20.dp), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = isChecked.value, onCheckedChange = { isChecked.value = it })
                    Text(text = "Don't ask again", color = Color(0XFF05A8B3))
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(isChecked.value) }) {
                Text("Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = { onDismiss(isChecked.value) }) {
                Text("Cancel")
            }
        }
    )
}

private fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}