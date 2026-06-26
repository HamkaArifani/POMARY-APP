package com.example.pomaryapp.ui.home

import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.colorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pomaryapp.R
import com.example.pomaryapp.domain.repository.AuthRepository
import com.example.pomaryapp.ui.auth.LoginViewModel

@Composable
fun HomeScreen(
    navController: NavController
) {
    Card(
        colors = CardDefaults.cardColors(colorResource(R.color.auth_background))
    ) {
        Text(text = "Hello World this is POMARY home screen")
    }
}