package com.example.coldfiles.ui.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.coldfiles.ui.storage.BASE_PATH
import java.io.File

data class SearchUiState(
    val text: String = "",
    val files: List<File> = listOf(),
    val selectedFilters: List<SearchFilter> = listOf()
)

class SearchViewModel: ViewModel() {
    /** Variable that holds state visible in UI **/
    var searchUiState by mutableStateOf(SearchUiState())
        private set

    /** Changes text in uiState and searches for files **/
    fun processTextChange(text: String) {
        /* Could possibly be overwritten by value in searchFiles function
        * If so, possible workaround is to abort coroutine when text is changed
        * or separate text from the rest of uiState*/
        searchUiState = searchUiState.copy(
            text = text
        )
        searchFiles(text)
    }

    /** Searches for specified files from root directory **/
    private fun searchFiles(name: String) {
        val root = File(BASE_PATH)
        searchUiState = searchUiState.copy(
            files = searchFilesRecursively(root, name)
        )
    }

    /** Searches for specified file from specified directory recursively**/
    private fun searchFilesRecursively(dir: File, name: String): List<File> {
        val result = mutableListOf<File>()
        val files = dir.listFiles() ?: return result

        for (file in files) {
            if (file.isDirectory) {
                result.addAll(searchFilesRecursively(file, name))
            } else if (file.name.contains(name, ignoreCase = true)) {
                result.add(file)
            }
        }
        return filterResults(result)
    }

    /** Selects the filter if it is not selected and deselects the filter if it is selected **/
    fun processFilterClick(filter: SearchFilter) {
        // Create updated list of filters
        val filterList = when(checkIfFilterSelected(filter)) {
            true -> searchUiState.selectedFilters - filter
            false -> searchUiState.selectedFilters + filter
        }

        // Update uiState
        searchUiState = searchUiState.copy(
            selectedFilters = filterList,
        )

        // Search files with new filtering
        searchFiles(searchUiState.text)
    }

    /** Checks if filter is in selectedFilters list**/
    fun checkIfFilterSelected(filter: SearchFilter): Boolean {
        return searchUiState.selectedFilters.contains(filter)
    }

    /** Returns full list if there's no filters, else uses filters from list **/
    private fun filterResults(
        files: List<File>,
        filterList: List<SearchFilter> = searchUiState.selectedFilters
    ): List<File> {
        if (searchUiState.selectedFilters.isEmpty()) return files

        val filteredFiles = mutableListOf<File>()
        for (file in files) {
            for (filter in filterList) {
                if (filter.extensions.contains(file.extension)) {
                    filteredFiles.add(file)
                    break
                }
            }
        }

        return filteredFiles
    }
}