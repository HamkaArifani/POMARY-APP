package com.example.pomaryapp.core.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.pomaryapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    state: DateRangePickerState,
    onConfirm: ()-> Unit,
    onDismiss: ()-> Unit
) {
   DatePickerDialog(
       onDismissRequest = onDismiss,
       confirmButton = {
           TextButton(onClick = onConfirm) {
               Text(stringResource(R.string.save))
           }
       },
       dismissButton = {
           TextButton(onClick = onConfirm) {
               Text(stringResource(R.string.cancel))
           }
       }
   ) {
       DateRangePicker(
           state = state,
           modifier = Modifier.weight(1f),
           title = { Text(stringResource(R.string.select_date_btn), modifier = Modifier.padding(16.dp)) }
       )
   }
}