package com.example.coldfiles.ui.storage

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.R
import com.example.coldfiles.ui.components.CircularCheckbox
import java.io.File
import java.text.DateFormat

/** Top-level composable that holds reference to uiState **/
@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    viewModel: StorageViewModel = viewModel()
) {
    val uiState = viewModel.storageUiState
    val context = LocalContext.current

    var selectedDialog: SelectedDialog by remember { mutableStateOf(SelectedDialog.NoDialog) }
    var selectedBottomBar: SelectedBottomBar by remember { mutableStateOf(SelectedBottomBar.NoBar) }

    StorageDialogSelector(
        selectedDialog = selectedDialog,
        onDismissRequest = { selectedDialog = SelectedDialog.NoDialog },
        onConfirmation = {
            selectedBottomBar = SelectedBottomBar.NoBar
            selectedDialog = SelectedDialog.NoDialog
        },
        viewModel = viewModel
    )

    BackHandler {
        if (selectedBottomBar == SelectedBottomBar.ContextBar) {
            selectedBottomBar = SelectedBottomBar.NoBar
            viewModel.resetItemSelection()
        } else {
            viewModel.moveToPreviousDirectory()
            if (uiState.pathDeque.isEmpty())
                (context as? Activity)?.finish()
        }
    }

    Scaffold(
        topBar = { StorageTopBar() },
        bottomBar = {
            AnimatedVisibility(
                visible = selectedBottomBar != SelectedBottomBar.NoBar,
                enter = slideInVertically(
                    initialOffsetY = { it / 2 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it / 2 },
                )
            ) {
                StorageBottomBar(
                    selectedBottomBar = selectedBottomBar,
                    onCopyClick = {
                        viewModel.createSavedFiles()
                        selectedBottomBar = SelectedBottomBar.CopyBar
                    },
                    onMoveClick = { /* TODO */ },
                    onShareClick = { /* TODO */ },
                    onDeleteClick = {
                        selectedDialog = SelectedDialog.DeleteDialog
                    },
                    onMoreClick = { /* TODO */ },
                    onCancelClick = {
                        selectedBottomBar = SelectedBottomBar.NoBar
                        viewModel.resetSavedItems()
                    },
                    savedItems = viewModel.savedFiles,

                    )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier.padding(innerPadding)
        ) {
            StorageScrollableBar(
                directoriesNames = viewModel.storageUiState.pathDeque,
                onBarItemClick = viewModel::moveToPreviousSpecifiedDirectory,
                modifier = Modifier.padding(16.dp)
            )
            StorageScreenCard(
                files = uiState.files,
                onItemClick = { item ->
                    if (selectedBottomBar == SelectedBottomBar.ContextBar) {
                        viewModel.updateItemSelection(item)
                    } else {
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
                    }

                },
                onLongItemClick = {
                    selectedBottomBar = SelectedBottomBar.ContextBar
                    viewModel.updateItemSelection(it)
                },
                showCheckBoxes = selectedBottomBar == SelectedBottomBar.ContextBar,
                checkSelection = viewModel::checkItemSelection
            )
        }
    }
}

/** Card containing grid of StorageItems **/
@Composable
fun StorageScreenCard(
    files: List<File>,
    onItemClick: (File) -> Unit,
    onLongItemClick: (File) -> Unit,
    showCheckBoxes: Boolean,
    checkSelection: (File) -> Boolean,
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
                    onLongItemClick = onLongItemClick,
                    showCheckBoxes = showCheckBoxes,
                    checkSelection = checkSelection
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

/** Grid of StorageItems **/
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StorageItemGrid(
    files: List<File>,
    onItemClick: (File) -> Unit,
    onLongItemClick: (File) -> Unit,
    showCheckBoxes: Boolean,
    checkSelection: (File) -> Boolean,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        modifier = modifier
    ) {
        items(files) { file ->
            StorageItem(
                file = file,
                showCheckBox = showCheckBoxes,
                checkSelection = checkSelection,
                modifier = Modifier.combinedClickable(
                    onClick = { onItemClick(file) },
                    onLongClick = { onLongItemClick(file) },
                )
            )
        }
    }
}

/** UI element that contains file/directory information
 * and provides ways to interact with it **/
@Composable
fun StorageItem(
    file: File,
    showCheckBox: Boolean,
    checkSelection: (File) -> Boolean,
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
        AnimatedVisibility(
            visible = showCheckBox,
            enter = slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + expandHorizontally(
                animationSpec = tween(delayMillis = 300)
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(durationMillis = 300)
            ) + shrinkHorizontally(
                animationSpec = tween(delayMillis = 300)
            )
        ) {
            CircularCheckbox(
                checked = checkSelection(file),
                modifier = Modifier
                    .padding(
                        bottom = 12.dp,
                        end = 16.dp
                    )
                    .size(20.dp)
            )
        }
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

/** Bar containing list of previous destinations **/
@Composable
fun StorageScrollableBar(
    directoriesNames: List<String>,
    modifier: Modifier = Modifier,
    onBarItemClick: (String) -> Unit
) {
    LazyRow(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        items(directoriesNames) { directory ->
            if (directory == "") {
                Icon(
                    painter = painterResource(id = R.drawable.folder),
                    contentDescription = "Home",
                    modifier = Modifier
                        .clickable { onBarItemClick(directory) }
                        .padding(4.dp)
                )
            } else {
                Text(
                    // Display "Home for root folder and directory names for others
                    text = directory,
                    modifier = Modifier.clickable { onBarItemClick(directory) }
                )
            }
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }
}

/** Top bar with**/
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