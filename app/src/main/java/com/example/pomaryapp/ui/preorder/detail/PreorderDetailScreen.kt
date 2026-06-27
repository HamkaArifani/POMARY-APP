package com.example.pomaryapp.ui.preorder.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun PreorderDetailScreen(preorderId: String, navController: NavController) {
    Scaffold(topBar = { Text("Detail Preorder ID: $preorderId") }) { padding ->
        Column(Modifier.padding(padding)) {
            Text("Halaman Detail Preorder")
            Button(onClick = { navController.navigate("preorder_form?preorderId=$preorderId") }) {
                Text("Edit Preorder ini")
            }
        }
    }
}