package com.example.pomaryapp.ui.order.form

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.pomaryapp.core.components.PomaryTextField

@Composable
fun OrderFormScreen(
    preorderId: String,
    orderId: String? = null,
    navController: NavController,
    viewModel: OrderFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val isEdit = orderId != null

    LaunchedEffect(orderId) {
        viewModel.initPrice(preorderId)
        orderId?.let { viewModel.loadOrderData(it) }
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
        ) {
            Text(
                text = stringResource(if (isEdit) R.string.edit_order else R.string.add_order_btn),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.header_card)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PomaryTextField(
                value = viewModel.buyerName,
                onValueChange = { viewModel.buyerName = it },
                label = stringResource(R.string.buyer_name)
            )

            Spacer(modifier = Modifier.height(16.dp))

            PomaryTextField(
                value = viewModel.buyerPhone,
                onValueChange = { viewModel.buyerPhone = it },
                label = stringResource(R.string.buyer_phone),
                keyboardType = KeyboardType.Phone
            )

            Spacer(modifier = Modifier.height(16.dp))

            PomaryTextField(
                value = viewModel.buyerQuantity,
                onValueChange = { viewModel.buyerQuantity = it },
                label = stringResource(R.string.total_order),
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(32.dp))

            PomaryButton(
                text = stringResource(R.string.save),
                onClick = {
                    viewModel.saveOrder(preorderId, orderId) {
                        Toast.makeText(context, R.string.success_save, Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }
                }
            )

            if (isEdit) {
                Spacer(modifier = Modifier.height(8.dp))
                PomaryButton(
                    text = stringResource(R.string.delete),
                    containerColor = Color.Red,
                    onClick = {
                        viewModel.deleteOrder(orderId!!) {
                            navController.popBackStack()
                        }
                    }
                )
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.cancel), color = Color.Gray)
            }
        }
    }
}