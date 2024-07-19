package com.example.coldfiles.ui

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

data class StorageUiState(
    val currentPath: String = "",
    val fileNames: List<File> = listOf()
)

class StorageViewModel : ViewModel() {
    var storageUiState by mutableStateOf(StorageUiState())
        private set

    init {
        getFilesList()
    }

    fun getFilesList() {
        val path = Environment.getExternalStorageDirectory().toString() +
                storageUiState.currentPath
        val directory = File(path)
        val files = directory.listFiles()
        if (files != null) {
            storageUiState = storageUiState.copy(
                fileNames = files.map { it }
            )
        }
    }
}