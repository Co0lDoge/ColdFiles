package com.example.coldfiles.ui.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.storage.StorageItemGrid
import com.example.coldfiles.ui.storage.openFile
import java.io.File

@Composable
fun SearchScreen(
    navigateAction: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState = viewModel.searchUiState
    val context = LocalContext.current

    Scaffold(
        topBar = { SearchTopBar(navigateAction, viewModel::searchFiles) },
        modifier = modifier
    ) { innerPadding ->
        SearchScreenCard(
            files = uiState.files,
            onItemClick = { openFile(it, context) },
            onLongItemClick = { },
            showCheckBoxes = false,
            checkSelection = { false },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

/** Card containing grid of StorageItems **/
@Composable
fun SearchScreenCard(
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
            modifier = Modifier.fillMaxSize()
        ) {
            Column {
                SearchFilterBar(Modifier.padding(16.dp))
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                )
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
                        text = "No matches",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SearchFilterBar(modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.padding(top = 16.dp))
        Text(
            text = "Type",
            style = MaterialTheme.typography.titleSmall
        )
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(SearchFilter.entries.toList()) { filter ->
                OutlinedButton(onClick = { /*TODO*/ }) {
                    Text(text = filter.name)
                }
            }
        }
    }
}

/** Search bar for text entry **/
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    navigateAction: () -> Unit,
    searchAction: (String) -> Unit,
) {
    // Text value for TextField
    val text = remember { mutableStateOf(TextFieldValue("")) }

    // Focus reference required to focus on search bar when search appears
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    // Controls whether the search bar is focused
    val focusManager = LocalFocusManager.current

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = navigateAction) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        title = {
            OutlinedTextField(
                value = text.value,
                onValueChange = {
                    text.value = it
                    searchAction(text.value.text)
                },
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                },
                modifier = Modifier.focusRequester(focusRequester),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    )
}