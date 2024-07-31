package com.example.coldfiles.ui.storage

import android.app.Activity
import android.content.Context
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
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.R
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
                onDirectoryClick = viewModel::moveToDirectory,
                context = context
            )
        }
    }
}

@Composable
fun StorageScreenContent(
    files: List<File>,
    onDirectoryClick: (String) -> Unit,
    context: Context,
    modifier: Modifier = Modifier,
) {
    Card {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = modifier.padding(16.dp)
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
                                openFile(file, context)
                            }

                            false -> {
                                onDirectoryClick(file.name)
                            }
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
    isFile: Boolean,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Icon(
            painter = if (isFile) painterResource(id = R.drawable.file)
                      else painterResource(id = R.drawable.folder),
            contentDescription = fileName,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Column {
            Text(text = fileName)
            Text("additional info")
            Spacer(modifier = Modifier)
            HorizontalDivider()
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