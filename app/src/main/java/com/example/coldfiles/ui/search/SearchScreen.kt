package com.example.coldfiles.ui.search

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.ui.storage.StorageScreenCard
import com.example.coldfiles.ui.storage.openFile

@Composable
fun SearchScreen(
    navigateAction: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = viewModel()
) {
    val uiState = viewModel.searchUiState
    val context = LocalContext.current

    val text = remember { mutableStateOf(TextFieldValue("")) }
    Scaffold(
        topBar = { SearchTopBar(text, navigateAction) },
        modifier = modifier
    ) { innerPadding ->
        StorageScreenCard(
            files = uiState.files,
            onItemClick = { openFile(it, context) },
            onLongItemClick = { },
            showCheckBoxes = false,
            checkSelection = { false },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    text: MutableState<TextFieldValue>,
    navigateAction: () -> Unit,
) {
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
                onValueChange = { text.value = it },
                placeholder = {
                    Text(
                        text = "Search",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Gray
                    )
                },
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                //textStyle = MaterialTheme.typography.bodyLarge
            )
        }
    )
}