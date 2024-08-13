package com.example.coldfiles.ui.storage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import com.example.coldfiles.ui.dialog.StorageAlertDialog

sealed interface SelectedDialog {
    data object NoDialog : SelectedDialog
    data object DeleteDialog : SelectedDialog
}

@Composable
fun StorageDialogSelector(
    selectedDialog: SelectedDialog,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: StorageViewModel
) {
    when (selectedDialog) {
        SelectedDialog.NoDialog -> {}
        SelectedDialog.DeleteDialog -> StorageAlertDialog(
            onDismissRequest = {
                onDismissRequest()
            },
            onConfirmation = {
                onConfirmation()
                viewModel.deleteItems()
            },
            dialogTitle = "File deletion alert",
            dialogText = "Confirm to delete these items: \n${
                viewModel.getSelectedItemNamesList().joinToString("\n")
            }",
            icon = Icons.Default.Warning
        )
    }
}