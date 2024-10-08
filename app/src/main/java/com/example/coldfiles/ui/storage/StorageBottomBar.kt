package com.example.coldfiles.ui.storage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.coldfiles.R
import com.example.coldfiles.ui.theme.ColdFilesTheme
import java.io.File

sealed interface SelectedBottomBar {
    data object NoBar : SelectedBottomBar
    data object ContextBar : SelectedBottomBar
    data object CopyBar : SelectedBottomBar
    data object MoveBar : SelectedBottomBar
}

@Composable
fun StorageBottomBar(
    onCopyClick: () -> Unit,
    onMoveClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoreClick: () -> Unit,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    selectedBottomBar: SelectedBottomBar,
    savedItems: List<File>,
    viewModel: StorageViewModel,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
        modifier = modifier
    ) {
        when (selectedBottomBar) {
            SelectedBottomBar.ContextBar -> {
                StorageBottomContextBar(
                    onCopyClick, onMoveClick, onShareClick, onDeleteClick, onMoreClick
                )
            }

            SelectedBottomBar.CopyBar -> {
                StorageCopyBar(
                    savedItems = savedItems,
                    onCancelClick = onCancelClick,
                    onConfirmClick = {
                        viewModel.copySavedItems()
                        onConfirmClick()
                    },
                    confirmText = "Copy Here"
                )
            }

            SelectedBottomBar.MoveBar -> {
                StorageCopyBar(
                    savedItems = savedItems,
                    onCancelClick = onCancelClick,
                    onConfirmClick = {
                        viewModel.moveSavedItems()
                        onConfirmClick()
                    },
                    confirmText = "Move Here"
                )
            }

            SelectedBottomBar.NoBar -> {}
        }
    }
}

/** Context menu that slides down from the bottom of the screen **/
@Composable
fun StorageBottomContextBar(
    onCopyClick: () -> Unit,
    onMoveClick: () -> Unit,
    onShareClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
            )
            .fillMaxWidth()
    ) {
        StorageBottomBarItem(
            text = "Copy",
            action = { onCopyClick() },
            iconRes = R.drawable.content_copy
        )
        StorageBottomBarItem(
            text = "Move",
            action = { onMoveClick() },
            iconRes = R.drawable.content_move
        )
        StorageBottomBarItem(
            text = "Share",
            action = { onShareClick() },
            iconRes = R.drawable.share
        )
        StorageBottomBarItem(
            text = "Delete",
            action = { onDeleteClick() },
            iconRes = R.drawable.delete
        )
        StorageBottomBarItem(
            enabled = false, // TODO: enable when working on more content for bottom bar
            text = "More",
            action = { onMoreClick() },
            iconRes = R.drawable.more_vertical
        )
    }
}

@Composable
fun StorageCopyBar(
    savedItems: List<File>,
    onCancelClick: () -> Unit,
    onConfirmClick: () -> Unit,
    confirmText: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(
                start = 8.dp,
                end = 8.dp,
            )
            .fillMaxWidth()
            .height(64.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painterResource(R.drawable.folder), contentDescription = "file")
            Text(
                text = "${savedItems.count()} item",
                style = MaterialTheme.typography.titleSmall
            )
        }

        Row {
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.clickable {
                    onCancelClick()
                }
            ) {
                Text(
                    text = "Cancel",
                    modifier = Modifier.padding(8.dp)
                )

            }
            Surface(
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.clickable {
                    onConfirmClick()
                }
            ) {
                Text(
                    text = confirmText,
                    modifier = Modifier.padding(8.dp)
                )
            }

        }
    }
}

/** Items in bottom bar **/
@Composable
fun StorageBottomBarItem(
    text: String,
    action: () -> Unit,
    enabled: Boolean = true,
    iconRes: Int
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = if (enabled)
            contentColorFor(MaterialTheme.colorScheme.surfaceContainerLow)
        else
            Color.Gray,
        shape = RoundedCornerShape(20.dp),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(enabled) { action() }
                .padding(
                    start = 12.dp,
                    top = 4.dp,
                    end = 12.dp,
                    bottom = 4.dp
                )
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "TODO",
                modifier = Modifier
                    .size(32.dp)
            )
            Text(text)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StorageBottomContextBarPreview() {
    ColdFilesTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(20.dp),
        ) {
            StorageBottomContextBar(
                onCopyClick = {},
                onMoveClick = {},
                onShareClick = {},
                onDeleteClick = {},
                onMoreClick = {}
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun StorageCopyBarPreview() {
    ColdFilesTheme {
        Surface(
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shape = RoundedCornerShape(20.dp),
        ) {
            StorageCopyBar(
                savedItems = listOf(),
                onCancelClick = { },
                onConfirmClick = { },
                confirmText = "Copy Here"
            )
        }
    }
}