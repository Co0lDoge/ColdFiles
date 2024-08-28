package com.example.coldfiles.ui.storage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import com.example.coldfiles.ui.dialog.StorageAlertDialog
import com.example.coldfiles.ui.dialog.StorageTextInputDialog

sealed interface SelectedDialog {
    data object NoDialog : SelectedDialog
    data object DeleteDialog : SelectedDialog
    data object CreateFileDialog : SelectedDialog
}

@Composable
fun StorageDialogSelector(
    selectedDialog: SelectedDialog,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    viewModel: StorageViewModel // Provide viewModel of StorageScreen to use it's data
) {
    when (selectedDialog) {
        SelectedDialog.NoDialog -> {}
        SelectedDialog.DeleteDialog -> StorageAlertDialog(
            onDismissRequest = onDismissRequest,
            onConfirmation = {
                onConfirmation()
                viewModel.deleteSelectedItems()
            },
            dialogTitle = "File deletion alert",
            dialogText = "Confirm to delete these items: \n${
                viewModel.getSelectedItemNamesList().joinToString("\n")
            }",
            icon = Icons.Default.Warning
        )

        SelectedDialog.CreateFileDialog -> StorageTextInputDialog(
            dialogTitle = "Enter file name",
            onConfirmation = viewModel::createFile,
            onDismiss = onDismissRequest
        )
    }
}