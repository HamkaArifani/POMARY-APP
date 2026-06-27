package com.example.pomaryapp.ui.preorder.active

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ActiveListScreen(navController: NavController) {
    Scaffold(topBar = { Text("Daftar Preorder Aktif") }) { padding ->
        LazyColumn(Modifier.padding(padding)) {
            items(10) { index ->
                Text("Item Aktif #$index", Modifier.clickable {
                    navController.navigate("preorder_detail/id_$index")
                }.padding(16.dp))
            }
        }
    }
}