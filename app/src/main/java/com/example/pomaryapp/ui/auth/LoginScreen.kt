package com.example.pomaryapp.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomaryapp.R
import com.example.pomaryapp.core.components.PomaryCard
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.pomaryapp.core.components.PomaryButton

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit
) {
    val state = viewModel.loginState

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.auth_background)),
        contentAlignment = Alignment.Center
    ){
        PomaryCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pomary_logo),
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.app_about),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(32.dp))

            if (state.isLoading) {
                CircularProgressIndicator(color = colorResource(id = R.color.auth_background))
            } else {
                PomaryButton(
                    text = stringResource(R.string.auth_text),
                    onClick = { viewModel.signIn() }
                )
            }

            state.error?.let {
                Text(
                    text = it.asString(),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    LaunchedEffect(state.user) {
        if (state.user != null) onLoginSuccess()
    }
}