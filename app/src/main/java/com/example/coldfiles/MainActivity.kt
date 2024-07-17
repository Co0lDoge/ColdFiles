package com.example.coldfiles

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.coldfiles.ui.StorageScreen
import com.example.coldfiles.ui.theme.ColdFilesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ColdFilesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    StorageScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}