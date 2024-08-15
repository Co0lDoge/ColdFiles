package com.example.coldfiles.ui.storage

import android.os.Environment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import java.io.File
import java.security.InvalidParameterException

val BASE_PATH = Environment.getExternalStorageDirectory().toString()

data class StorageUiState(
    // Defaults to root folder path
    val pathDeque: ArrayDeque<String> = ArrayDeque(),
    val files: List<File> = listOf(),
    val selectedIndexes: SnapshotStateList<Int> = mutableStateListOf()
)

class StorageViewModel : ViewModel() {
    /** Variable that holds state visible in UI **/
    var storageUiState by mutableStateOf(StorageUiState())
        private set

    /** Saved files used for copying **/
    var savedFiles = listOf<File>()

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
            // Currently folders and files are sorted by name with folders on top
            // TODO: Add ability to select sort type and order.
            storageUiState.copy(
                files = files
                    .map { it }
                    .sortedWith(compareBy<File> { it.isFile }
                        .thenBy { it.name })
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

    /** Check if current item's index present in selectedIndexes list **/
    fun checkItemSelection(item: File): Boolean {
        val itemIndex = storageUiState.files.indexOf(item)
        return storageUiState.selectedIndexes.contains(itemIndex)
    }

    /** Adds or removes current item's index in selectedIndexes list **/
    fun updateItemSelection(item: File) {
        val itemIndex = storageUiState.files.indexOf(item)
        if (storageUiState.selectedIndexes.contains(itemIndex)) {
            storageUiState.selectedIndexes.remove(itemIndex)
            return
        }
        storageUiState.selectedIndexes.add(itemIndex)
    }

    /** Removes all indexes from selectedIndexes list **/
    fun resetItemSelection() {
        storageUiState.selectedIndexes.clear()
    }

    /** Returns list of all selected files names **/
    fun getSelectedItemNamesList(): List<String> {
        return storageUiState.files.slice(storageUiState.selectedIndexes).map { it.name }
    }

    /** Delete all selected items **/
    fun deleteSelectedItems() {
        val selectedItems = storageUiState.files.slice(storageUiState.selectedIndexes)
        selectedItems.forEach { item ->
            deleteItem(item)
        }

        // Move to the same directory to trigger recomposition
        resetItemSelection()
        moveToDirectory()
    }

    /** Delete items, if it's a folder, delete all nested items **/
    private fun deleteItem(item: File): Boolean {
        if (item.exists()) {
            if (item.isDirectory) {
                val files = item.listFiles()
                for (file in files!!) {
                    deleteItem(file)
                }
            }
            return item.delete()
        }
        return false
    }

    /** Saves selected files for future use (Copying or Moving) **/
    fun createSavedFiles() {
        savedFiles = storageUiState.files.slice(storageUiState.selectedIndexes).toList()
    }

    /** Remove all items from savedFiles **/
    fun resetSavedItems() {
        savedFiles = listOf()
    }

    /** Copy saved files to current path **/
    fun copySavedItems() {
        savedFiles.forEach { file ->
            file.copyTo(File(fullPath + "/" + file.name))
        }
        moveToDirectory()
    }
}