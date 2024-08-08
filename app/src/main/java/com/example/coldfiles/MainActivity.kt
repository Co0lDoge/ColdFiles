package com.example.coldfiles

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.coldfiles.ui.dialog.StorageAlertDialog
import com.example.coldfiles.ui.storage.StorageScreen
import com.example.coldfiles.ui.theme.ColdFilesTheme

class MainActivity : ComponentActivity() {

    private var isActivityLaunched = false

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launchStorageApp()
    }

    override fun onRestart() {
        super.onRestart()
        Log.v("StorageActivity", "Activity restarted")
        if (!isActivityLaunched) {
            Log.v("StorageActivity", "Activity relaunched")
            launchStorageApp()
        }
    }

    /** Check storage permission,
     * if granted, launch the StorageScreen,
     * else show dialog asking for permission**/
    private fun launchStorageApp() {
        setContent {
            ColdFilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (Environment.isExternalStorageManager()) {
                        isActivityLaunched = true
                        StorageScreen()
                    } else {
                        enableEdgeToEdge()
                        StorageAlertDialog(
                            onDismissRequest = { finish() },
                            onConfirmation = {
                                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                                ContextCompat.startActivity(this, intent, null)
                            },
                            dialogTitle = "Storage permission required",
                            dialogText = "This is a file manager that requires access to all files." +
                                    " Press Confirm to grant permission" +
                                    " or Dismiss to exit the application.",
                            icon = Icons.Default.Warning
                        )
                    }
                }
            }
        }

    }
}