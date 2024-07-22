package com.example.coldfiles.ui

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    viewModel: StorageViewModel = viewModel()
) {
    val uiState = viewModel.storageUiState
    val context = LocalContext.current

    Scaffold(
        topBar = { StorageTopBar() }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier.padding(innerPadding)
        ) {
            items(uiState.fileNames) { file ->
                FileCard(
                    fileName = file.name,
                    modifier = Modifier.clickable {
                        // If file is clicked, open it
                        // Else move to selected directory
                        when (file.isFile) {
                            true -> {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.data = file.toUri()
                                context.startActivity(intent)
                            }

                            false -> viewModel.moveToDirectory(file.path)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FileCard(
    fileName: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Email,
            contentDescription = fileName,
            modifier = modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Column {
            Text(text = fileName)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StorageTopBar() {
    CenterAlignedTopAppBar(
        title = { },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = null)
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = null)
            }
        }
    )
}

@Composable
@Preview(showBackground = true)
fun FileCardPreview() {
    ColdFilesTheme {
        FileCard(fileName = "File Name")
    }
}

//@Composable
//@Preview(showBackground = true)
//fun StorageScreenPreview() {
//    ColdFilesTheme {
//        StorageScreen()
//    }
//}