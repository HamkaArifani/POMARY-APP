package com.example.pomaryapp.ui.preorder.detail

import android.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.pomaryapp.domain.model.PreorderModel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.TextButton
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pomaryapp.R
import com.example.pomaryapp.core.utils.toRupiah

@Composable
fun RecapDialog(
    preorder: PreorderModel,
    onDismiss : ()-> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.action_recap),
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                RecapRow(label = stringResource(R.string.initial_capital), value = preorder.initialCapital.toRupiah())
                RecapRow(label = stringResource(R.string.gross_profit), value = preorder.totalGrossProfit.toRupiah())

                HorizontalDivider(thickness = 1.dp, color = Color.LightGray)

                RecapRow(
                    label = stringResource(R.string.clean_profit),
                    value = preorder.totalCleanProfit.toRupiah(),
                    isBold = true,
                    valueColor = colorResource(R.color.header_card)
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.okay))
            }
        }
    )
}

@Composable
private fun RecapRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    valueColor: Color = Color.Black
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = valueColor
        )
    }
}