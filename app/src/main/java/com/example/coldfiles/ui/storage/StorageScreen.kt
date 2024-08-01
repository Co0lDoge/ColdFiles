package com.example.coldfiles.ui.storage

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.R
import java.io.File
import java.text.DateFormat


@Composable
        /** Top-level composable that holds reference to uiState **/
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
        Box(modifier = modifier
            .padding(innerPadding)
            .fillMaxHeight()) {
            Column {
                StorageScrollableBar(
                    directoriesNames = viewModel.storageUiState.pathDeque,
                    onBarItemClick = viewModel::moveToPreviousSpecifiedDirectory,
                    modifier = Modifier.padding(16.dp)
                )
                StorageScreenCard(
                    files = uiState.files,
                    onItemClick = { item ->
                        when (item.isFile) {
                            // If item is file, open it
                            true -> {
                                openFile(item, context)
                            }
                            // Otherwise, if it's a directory, go to that directory
                            false -> {
                                viewModel.moveToDirectory(item.name)
                            }
                        }
                    },
                    onLongItemClick = {/*TODO*/ },
                )
            }
            StorageContextMenu(modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
fun StorageContextMenu(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.padding(
                start = 16.dp,
                top = 4.dp,
                end = 16.dp,
                bottom = 4.dp
            )
        ) {
            items(5) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { /* TODO and separate into column into function*/ }
                            .padding(
                                start = 8.dp,
                                top = 4.dp,
                                end = 8.dp,
                                bottom = 4.dp
                            )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.folder),
                            contentDescription = "TODO",
                            modifier = Modifier
                                .size(32.dp)
                        )
                        Text(text = "Action")
                    }
                }
            }
        }
    }
}

@Composable
        /** Card containing grid of StorageItems **/
fun StorageScreenCard(
    files: List<File>,
    onItemClick: (File) -> Unit,
    onLongItemClick: (File) -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .padding(bottom = 8.dp)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            if (files.isNotEmpty()) {
                StorageItemGrid(
                    files = files,
                    onItemClick = onItemClick,
                    onLongItemClick = onLongItemClick
                )
            } else {
                Text(
                    text = "Directory is empty",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
        /** Grid of StorageItems **/
fun StorageItemGrid(
    files: List<File>,
    onItemClick: (File) -> Unit,
    onLongItemClick: (File) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
    ) {
        items(files) { file ->
            StorageItem(
                file = file,
                modifier = Modifier.combinedClickable(
                    onClick = { onItemClick(file) },
                    onLongClick = { onLongItemClick(file) },
                )
            )
        }
    }
}

@Composable
        /** UI element that contains file/directory information
         * and provides ways to interact with it **/
fun StorageItem(
    file: File,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                end = 16.dp
            )
    ) {
        Icon(
            painter = if (file.isFile) painterResource(id = R.drawable.file)
            else painterResource(id = R.drawable.folder),
            contentDescription = file.name,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(32.dp)
        )
        Spacer(modifier = Modifier.padding(horizontal = 16.dp))
        Column {
            Text(text = file.name)
            Text(text = DateFormat.getDateInstance().format(file.lastModified()))
            Spacer(modifier = Modifier.padding(bottom = 8.dp))
            HorizontalDivider()
        }
    }
}

@Composable
        /** Bar containing list of previous destinations **/
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
        /** Top bar with**/
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