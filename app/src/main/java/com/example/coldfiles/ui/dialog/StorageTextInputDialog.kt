package com.example.coldfiles.ui.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun StorageTextInputDialog(
    dialogTitle: String,
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val text = remember { mutableStateOf(TextFieldValue("")) }
    Dialog(onDismiss) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = modifier
        ) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    Text(text = dialogTitle)
                    Spacer(Modifier.size(16.dp))
                    OutlinedTextField(
                        value = text.value,
                        onValueChange =  { text.value = it },
                    )
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    TextButton(
                        onClick = {
                            onConfirmation()
                        }
                    ) {
                        Text("Confirm")
                    }
                    TextButton(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Text("Dismiss")
                    }
                }
            }
        }
    }
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