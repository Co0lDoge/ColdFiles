package com.example.coldfiles.ui.storage

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.io.File
import java.security.InvalidParameterException

val BASE_PATH = Environment.getExternalStorageDirectory().toString()

data class StorageUiState(
    // Defaults to root folder path
    val pathDeque: ArrayDeque<String> = ArrayDeque(),
    val files: List<File> = listOf()
)

class StorageViewModel : ViewModel() {
    var storageUiState by mutableStateOf(StorageUiState())
        private set

    /** Combines root directory path with directories in deque **/
    private val fullPath
        get() = BASE_PATH + storageUiState.pathDeque.joinToString(
            separator = '/'.toString()
        )

    init {
        moveToDirectory("")
    }

    /** Moves to specified directory, if none is provided moves to current directory **/
    fun moveToDirectory(directoryName: String? = null) {
        if (directoryName != null) {
            storageUiState.pathDeque.add(directoryName)
        }

        val directory = File(fullPath)
        val files = directory.listFiles()
        storageUiState = if (files != null) {
            storageUiState.copy(
                files = files.map { it },
            )
        } else {
            storageUiState.copy(
                files = listOf()
            )
        }
    }

    /** Removes last directory from deque and moves to last remained **/
    fun moveToPreviousDirectory() {
        storageUiState.pathDeque.removeLastOrNull()
        moveToDirectory()
    }

    /** Moves to one of the directories from deque
     * if none **/
    fun moveToPreviousSpecifiedDirectory(directoryName: String) {
        // If it's not root and there's no such directory, throw Exception
        if (!storageUiState.pathDeque.contains(directoryName))
            throw InvalidParameterException("This directory doesn't exist")

        // Removes all directories until required is reached
        while (storageUiState.pathDeque.last() != directoryName) {
            storageUiState.pathDeque.removeLast()
        }

        moveToDirectory()
    }
}