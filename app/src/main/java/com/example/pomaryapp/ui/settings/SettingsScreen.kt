package com.example.pomaryapp.ui.settings

import android.provider.Settings.Global.getString
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pomaryapp.R
import com.example.pomaryapp.core.components.PomaryButton
import com.example.pomaryapp.core.components.PomaryCard
import com.example.pomaryapp.core.components.PomaryHeader
import com.example.pomaryapp.core.components.PomaryTextField

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            PomaryHeader(
                icon = Icons.Default.Home,
                iconLabel = stringResource(R.string.home),
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(colorResource(id = R.color.auth_card))
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(text = "Akun Pengguna", fontWeight = FontWeight.Bold)
            PomaryCard {
                PomaryTextField(
                    value = viewModel.name,
                    onValueChange = { viewModel.name = it },
                    label = "Nama Pemilik Usaha"
                )
            }

            Text(text = "Keamanan & Sesi", fontWeight = FontWeight.Bold)
            PomaryCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.showPinDialog = true }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Lock, null, tint = colorResource(R.color.header_card))
                    Spacer(Modifier.width(16.dp))
                    Text(text = "Ubah PIN Keamanan", modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
                }

                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.logout {
                                navController.navigate("login") { popUpTo(0) { inclusive = true } }
                            }
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color.Red)
                    Spacer(Modifier.width(16.dp))
                    Text(text = stringResource(R.string.sign_out), color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }

            Text(text = stringResource(R.string.chat_template), fontWeight = FontWeight.Bold)
            PomaryCard {
                PomaryTextField(
                    value = viewModel.messageTemplate,
                    onValueChange = { viewModel.messageTemplate = it },
                    label = "Pesan Koordinasi Pengantaran",
                    singleLine = false,
                    modifier = Modifier.heightIn(min = 100.dp)
                )
            }

            PomaryButton(
                text = stringResource(R.string.save),
                onClick = {
                    viewModel.saveProfile {
                        Toast.makeText(context, "Berhasil disimpan", Toast.LENGTH_SHORT).show()
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    if (viewModel.showPinDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.showPinDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updatePin {
                            Toast.makeText(context, R.string.update_pin_succes, Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = viewModel.newPinInput.length == 4
                ) { Text(stringResource(R.string.save_pin)) }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.showPinDialog = false }) { Text("Batal") }
            },
            title = { Text("Ubah PIN Baru") },
            text = {
                Column {
                    Text("Masukkan 4 digit kode keamanan baru kamu.")
                    Spacer(Modifier.height(16.dp))
                    PomaryTextField(
                        value = viewModel.newPinInput,
                        onValueChange = { if(it.length <= 4) viewModel.newPinInput = it },
                        label = "PIN Baru",
                        isPassword = true,
                        keyboardType = KeyboardType.Number
                    )
                }
            }
        )
    }
}