package com.example.coldfiles.ui.dialog

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun StorageTextInputDialog(
    dialogTitle: String,
    onConfirmation: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = remember { mutableStateOf(TextFieldValue("")) }
    AlertDialog(
        icon = {
            Icon(Icons.Default.Create, contentDescription = dialogTitle)
        },
        text = {
            Column {
                Text(text = dialogTitle)
                OutlinedTextField(
                    value = text.value,
                    onValueChange =  { text.value = it },
                )
            }
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(text.value.text)
                    onDismiss()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Dismiss")
            }
        },
        modifier = modifier,
    )
}

@Composable
@Preview(showBackground = true)
private fun StorageTextInputDialogPreview() {
    ColdFilesTheme {
        StorageTextInputDialog(
            dialogTitle = "Enter Name",
            onConfirmation = {},
            onDismiss = {}
        )
    }
}