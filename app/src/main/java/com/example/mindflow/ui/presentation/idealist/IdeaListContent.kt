package com.example.mindflow.ui.presentation.idealist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun IdeaListContent(
    uiState: IdeaListUiState,
    onIdeaClick: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Historial de Ideas",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 48.dp, bottom = 16.dp)
        )

        Box(modifier = Modifier.weight(1f)) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center).padding(16.dp)
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(uiState.ideas) { idea ->
                        ListItem(
                            headlineContent = { Text(text = idea.title, fontWeight = FontWeight.Medium) },
                            supportingContent = { Text(text = "Categoría: ${idea.category}") },
                            modifier = Modifier.clickable { onIdeaClick(idea.id) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                    }
                }
            }
        }
    }
}