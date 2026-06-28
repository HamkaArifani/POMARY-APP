package com.example.pomaryapp.ui.preorder.form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.pomaryapp.core.components.DateRangePickerDialog
import com.example.pomaryapp.core.components.PomaryButton
import com.example.pomaryapp.core.components.PomaryCard
import com.example.pomaryapp.core.components.PomaryTextField
import com.example.pomaryapp.core.utils.DateFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreorderFormScreen(
    preorderId: String?,
    navController: NavController,
    viewModel: PreorderFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isEdit = preorderId != null
    val showDatePicker = remember { mutableStateOf(false) }
    val dateState = rememberDateRangePickerState()

    LaunchedEffect(preorderId) {
        preorderId?.let { viewModel.loadData(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.auth_background)),
        contentAlignment = Alignment.Center
    ) {
        PomaryCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(if (isEdit) R.string.edit_po else R.string.create_po),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.header_card)
            )

            Spacer(modifier = Modifier.height(16.dp))

            FormStepLabel(stringResource(R.string.step_1))
            PomaryTextField(
                value = viewModel.title,
                onValueChange = { viewModel.title = it },
                label = stringResource(R.string.preorder_title)
            )
            Spacer(modifier = Modifier.height(8.dp))
            PomaryTextField(
                value = viewModel.productName,
                onValueChange = { viewModel.productName = it },
                label = stringResource(R.string.product_name)
            )

            FormStepLabel(stringResource(R.string.step_2))
            OutlinedButton(
                onClick = { showDatePicker.value = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            ) {
                val dateText = if (viewModel.startDate == null) stringResource(R.string.select_date_btn)
                else DateFormatter.formatDateRange(viewModel.startDate, viewModel.endDate)
                Text(text = dateText)
            }

            FormStepLabel(stringResource(R.string.step_3))
            PomaryTextField(
                value = viewModel.hpp,
                onValueChange = { viewModel.hpp = it },
                label = stringResource(R.string.production_cost),
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(8.dp))
            PomaryTextField(
                value = viewModel.sellingPrice,
                onValueChange = { viewModel.sellingPrice = it },
                label = stringResource(R.string.selling_price),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(32.dp))

            PomaryButton(
                text = stringResource(R.string.save),
                onClick = {
                    viewModel.save(
                        id = preorderId,
                        onSuccess = { navController.popBackStack() },
                        onError = { Toast.makeText(context, it.asString(context), Toast.LENGTH_SHORT).show() }
                    )
                }
            )

            if (isEdit) {
                Spacer(modifier = Modifier.height(8.dp))
                PomaryButton(
                    text = stringResource(R.string.delete),
                    containerColor = Color.Red,
                    onClick = {
                        viewModel.delete(preorderId) {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                )
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.cancel), color = Color.Gray)
            }
        }
    }

    if (showDatePicker.value) {
        DateRangePickerDialog(
            state = dateState,
            onDismiss = { showDatePicker.value = false },
            onConfirm = {
                viewModel.startDate = dateState.selectedStartDateMillis
                viewModel.endDate = dateState.selectedEndDateMillis
                showDatePicker.value = false
            }
        )
    }
}

@Composable
fun FormStepLabel(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 4.dp)
    )
}