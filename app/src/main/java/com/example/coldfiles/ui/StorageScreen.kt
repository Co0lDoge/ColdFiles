package com.example.coldfiles.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coldfiles.R
import com.example.coldfiles.ui.theme.ColdFilesTheme

@Composable
fun StorageScreen(
    modifier: Modifier = Modifier,
    viewModel: StorageViewModel = viewModel()
) {
    val uiState = viewModel.storageUiState
    Surface(modifier.fillMaxWidth()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = modifier
        ) {
            items(uiState.fileNames) { file ->
                FileCard(fileName = file.name)
            }
        }
    }
}

@Composable
fun FileCard(
    fileName: String,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "TODO"
            )
            Spacer(modifier = Modifier.padding(horizontal = 16.dp))
            Column {
                Text(text = fileName)
            }
        }
    }
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