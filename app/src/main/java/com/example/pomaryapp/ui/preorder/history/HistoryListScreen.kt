package com.example.pomaryapp.ui.preorder.history

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HistoryListScreen(navController: NavController) {
    Scaffold(topBar = { Text("Riwayat Preorder") }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(10) { index ->
                Text("Item Selesai #$index", Modifier.padding(16.dp))
            }
        }
    }
}