package com.example.coldfiles.ui.storage

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.coldfiles.ui.components.RoundedDropdownMenu

/** Top bar with**/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageTopBar(
    isMenuExpanded: Boolean,
    onItemClick: () -> Unit, // Action when clicking on any item
    onSearchClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCreateFileClick: () -> Unit,
    onCreateFolderClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onMenuDismissRequest: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = { },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
            IconButton(onClick = onMoreClick) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
            RoundedDropdownMenu(
                expanded = isMenuExpanded,
                onDismissRequest = onMenuDismissRequest
            ) {
                DropdownMenuItem(
                    text = { Text(text = "Create File") },
                    onClick = {
                        onCreateFileClick()
                        onItemClick()
                    }
                )
                DropdownMenuItem(
                    text = { Text(text = "Create Folder") },
                    onClick = {
                        onCreateFolderClick()
                        onItemClick()
                    }
                )
                HorizontalDivider()
                DropdownMenuItem(
                    text = { Text(text = "Settings") },
                    onClick = {
                        onSettingsClick()
                        onItemClick()
                    }
                )
            }
        }
    )
}