package com.example.pomaryapp.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(topBar = { Text("Pengaturan") }) { padding ->
        Column(Modifier.padding(padding)) {
            Text("Edit Nama, PIN, dan Template Pesan di sini")
        }
    }
}