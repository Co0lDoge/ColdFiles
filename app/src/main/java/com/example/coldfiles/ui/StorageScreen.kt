package com.example.coldfiles.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
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
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.theme.ColdFilesTheme
import java.io.File


@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    viewModel: StorageViewModel = viewModel()
) {
    val uiState = viewModel.storageUiState
    val context = LocalContext.current

    BackHandler {
        viewModel.moveToPreviousDirectory()
        if (uiState.pathDeque.isEmpty())
            (context as? Activity)?.finish()
    }

    Scaffold(
        topBar = { StorageTopBar() }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            StorageScrollableBar(
                directoriesNames = viewModel.storageUiState.pathDeque,
                onBarItemClick = viewModel::moveToPreviousSpecifiedDirectory,
                modifier = Modifier.padding(16.dp)
            )
            StorageScreenContent(
                files = uiState.files,
                onFileClick = viewModel::moveToDirectory,
                context = context
            )
        }
    }
}

@Composable
fun StorageScreenContent(
    files: List<File>,
    onFileClick: (String) -> Unit,
    context: Context,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
    ) {
        items(files) { file ->
            FileCard(
                fileName = file.name,
                isFile = file.isFile,
                modifier = Modifier.clickable {
                    // If file is clicked, open it
                    // Else move to selected directory
                    when (file.isFile) {
                        true -> {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = FileProvider.getUriForFile(
                                context,
                                context.applicationContext.packageName + ".provider",
                                file
                            )
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_ACTIVITY_NEW_TASK)
                            context.startActivity(intent)
                        }

                        false -> {
                            onFileClick(file.name)
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun FileCard(
    fileName: String,
    isFile: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(16.dp)
    ) {
        Icon(
            imageVector = if (isFile) Icons.Default.Lock else Icons.Default.Email,
            contentDescription = fileName,
            modifier = modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Column {
            Text(text = fileName)
        }
    }
}

@Composable
fun StorageScrollableBar(
    directoriesNames: List<String>,
    modifier: Modifier = Modifier,
    onBarItemClick: (String) -> Unit
) {
    LazyRow(modifier = modifier) {
        items(directoriesNames) { directory ->
            Text(
                // Display "Home for root folder and directory names for others
                text = if (directory == "") "Home" else directory,
                modifier = Modifier.clickable { onBarItemClick(directory) }
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
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
        FileCard(
            fileName = "File Name",
            isFile = false
        )
    }
}

//@Composable
//@Preview(showBackground = true)
//fun StorageScreenPreview() {
//    ColdFilesTheme {
//        StorageScreen()
//    }
//}