package com.example.coldfiles.ui

import android.os.Environment
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File

val BASE_PATH = Environment.getExternalStorageDirectory().toString()

data class StorageUiState(
    // Defaults to root folder path
    val pathDeque: ArrayDeque<String> = ArrayDeque(),
    val files: List<File> = listOf()
)

class StorageViewModel : ViewModel() {
    var storageUiState by mutableStateOf(StorageUiState())
        private set

    private val fullPath get() = BASE_PATH + storageUiState.pathDeque.joinToString(
            separator = '/'.toString()
        )

    init {
        moveToDirectory("")
    }

    fun moveToDirectory(directoryName: String? = null) {
        if (directoryName != null) {
            storageUiState.pathDeque.add(directoryName)
        }

        Log.d("FilesDebug", fullPath)

        val directory = File(fullPath)
        val files = directory.listFiles()
        if (files != null) {
            // Returns false if directory is empty
            storageUiState = storageUiState.copy(
                files = files.map { it },
            )
        }
    }

    fun moveToPreviousDirectory() {
        storageUiState.pathDeque.removeLastOrNull()
        moveToDirectory()
    }
}