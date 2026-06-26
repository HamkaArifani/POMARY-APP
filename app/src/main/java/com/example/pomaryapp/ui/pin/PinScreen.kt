package com.example.pomaryapp.ui.pin

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pomaryapp.R
import com.example.pomaryapp.core.components.PomaryButton
import com.example.pomaryapp.core.components.PomaryCard
import com.example.pomaryapp.core.components.PomaryTextField
import com.example.pomaryapp.core.utils.StringText
import timber.log.Timber

@Composable
fun PinScreen(
    isSetupMode: Boolean,
    viewModel: PinViewModel = hiltViewModel(),
    onSuccess: () -> Unit
) {
    val state = viewModel.pinState
    val lockoutMinutes by viewModel.lockoutMinutes.collectAsState()
    var pinInput by remember { mutableStateOf("") }
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
                painter = painterResource(id = R.drawable.logopomary),
                contentDescription = null,
                modifier = Modifier
                    .aspectRatio(2f)
                    .fillMaxWidth(0.5f),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.app_about),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if (isSetupMode) stringResource(R.string.new_pin) else stringResource(R.string.pin_default),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            PomaryTextField(
                value = pinInput,
                onValueChange = {
                    if (it.length <= 4) {
                        pinInput = it
                        Timber.d("DEBUG_PIN: Input saat ini = $it, Panjang = ${it.length}")
                    }
                },
                label = "4 Digit PIN",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (state.isLoading) {
                CircularProgressIndicator(color = colorResource(id = R.color.auth_background))
            } else {
                PomaryButton(
                    text = stringResource(R.string.confirmation),
                    enabled = pinInput.length == 4 && lockoutMinutes == 0L,
                    onClick = {
                        if (isSetupMode) viewModel.createPin(pinInput, onSuccess)
                        else viewModel.validatePin(pinInput, onSuccess)
                    }
                )
            }

            if(lockoutMinutes>0){
                Text(
                    text = StringText.StringResource(R.string.lockout_msg, lockoutMinutes).asString(),
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp),
                    textAlign = TextAlign.Center
                )
            } else if (state.error != null) {
                Text(
                    text = state.error?.asString() ?: "",
                    color = Color.Red,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}