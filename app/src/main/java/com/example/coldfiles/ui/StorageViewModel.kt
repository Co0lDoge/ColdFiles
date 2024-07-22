package com.example.coldfiles.ui

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

data class StorageUiState(
    // Defaults to root folder path
    val currentPath: String = Environment.getExternalStorageDirectory().toString(),
    val fileNames: List<File> = listOf()
)

class StorageViewModel : ViewModel() {
    var storageUiState by mutableStateOf(StorageUiState())
        private set

    init {
        moveToDirectory(storageUiState.currentPath)
    }

    fun moveToDirectory(path: String) {
        val directory = File(path)
        val files = directory.listFiles()
        if (files != null) {
            // Returns false if directory is empty
            storageUiState = storageUiState.copy(
                fileNames = files.map { it },
                currentPath = path
            )
        }
    }
}