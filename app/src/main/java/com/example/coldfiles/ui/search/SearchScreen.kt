package com.example.coldfiles.ui.search

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.storage.StorageScreenCard
import com.example.coldfiles.ui.storage.openFile

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState = viewModel.searchUiState
    val context = LocalContext.current

    StorageScreenCard(
        files = uiState.files,
        onItemClick = { openFile(it, context) },
        onLongItemClick = {  },
        showCheckBoxes = false,
        checkSelection = { false }
    )
}