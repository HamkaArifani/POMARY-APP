package com.example.pomaryapp.ui.preorder.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun PreorderFormScreen(preorderId: String?, navController: NavController) {
    val isEdit = preorderId != null
    Scaffold(topBar = { Text(if (isEdit) "Edit Preorder" else "Tambah Preorder") }) { padding ->
        Column(Modifier.padding(padding)) {
            Text(if (isEdit) "Mengedit data ID: $preorderId" else "Memasukkan data baru")
            Button(onClick = { navController.popBackStack() }) {
                Text("Simpan (Back)")
            }
        }
    }
}