package com.example.coldfiles

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            launchStorageApp()
        } else {
            Toast.makeText(this, "This app requires storage permission", Toast.LENGTH_SHORT).show()
        }
    }

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
        enableEdgeToEdge()
        setContent {
            ColdFilesTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (checkStoragePermissions()) {
                        isActivityLaunched = true
                        StorageScreen()
                    } else {
                        StorageAlertDialog(
                            onDismissRequest = { finish() },
                            onConfirmation = {
                                requestStoragePermission()
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

    private fun checkStoragePermissions(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            //Android is 11 (R) or above
            return Environment.isExternalStorageManager()
        } else {
            //Below android 11
            val write =
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
            val read =
                ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                )

            return read == PackageManager.PERMISSION_GRANTED && write == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            ContextCompat.startActivity(this, intent, null)
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            requestPermissionLauncher.launch(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }
}