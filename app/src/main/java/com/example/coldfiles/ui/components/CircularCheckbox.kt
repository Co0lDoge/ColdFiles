package com.example.coldfiles.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun CircularCheckbox(
    modifier: Modifier = Modifier,
    checked: Boolean,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = CircleShape
            )
            .background(
                color = if (checked)
                    MaterialTheme.colorScheme.onSecondary
                else
                    Color.Transparent,
                shape = CircleShape
            ),

        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
private fun CircularCheckboxPreview() {
    ColdFilesTheme {
        CircularCheckbox(checked = true)
    }
}