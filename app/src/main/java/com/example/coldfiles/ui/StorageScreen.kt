package com.example.coldfiles.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun StorageScreen(
    modifier: Modifier,
    viewModel: StorageViewModel = viewModel()
) {
    LazyColumn(
        modifier = modifier
    ) {

    }
}

@Composable
@Preview(showBackground = true)
fun StorageScreenPreview() {
    ColdFilesTheme {
        StorageScreen(Modifier)
    }
}