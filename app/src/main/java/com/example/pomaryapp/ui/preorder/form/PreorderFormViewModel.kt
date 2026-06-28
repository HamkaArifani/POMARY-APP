package com.example.pomaryapp.ui.preorder.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomaryapp.R
import com.example.pomaryapp.core.utils.StringText
import com.example.pomaryapp.domain.model.PreorderModel
import com.example.pomaryapp.domain.usecase.preorder.DeletePreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.FinishPreorderUseCase
import com.example.pomaryapp.domain.usecase.preorder.GetPreorderDetailUseCase
import com.example.pomaryapp.domain.usecase.preorder.UpsertPreorderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PreorderFormViewModel @Inject constructor(
    private val deleteUseCase: DeletePreorderUseCase,
    private val upsertPreorderUseCase: UpsertPreorderUseCase,
    private val getPreorderDetailUseCase: GetPreorderDetailUseCase
): ViewModel(){
    var title by mutableStateOf("")
    var productName by mutableStateOf("")
    var hpp by mutableStateOf("")
    var sellingPrice by mutableStateOf("")
    var startDate by mutableStateOf<Long?>(null)
    var endDate by mutableStateOf<Long?>(null)
    var isLoading by mutableStateOf(false)
    var isCompleted by mutableStateOf(false)

    fun loadData(id: String) {
        viewModelScope.launch {
            getPreorderDetailUseCase(id)?.let {
                title = it.title
                productName = it.productName
                hpp = it.totalCost.toString()
                sellingPrice = it.sellingPrice.toString()
                startDate = it.startDate
                endDate = it.endDate
                isCompleted = it.isCompleted
            }
        }
    }

    fun save(id: String?, onSuccess: () -> Unit, onError: (StringText) -> Unit) {
        if (title.isBlank() || productName.isBlank() || startDate == null || endDate == null) {
            onError(StringText.StringResource(R.string.error_empty_fields))
            return
        }
        viewModelScope.launch {
            val model = PreorderModel(
                preorderId = id ?: UUID.randomUUID().toString(),
                title = title,
                productName = productName,
                totalCost = hpp.toLongOrNull() ?: 0L,
                sellingPrice = sellingPrice.toLongOrNull() ?: 0L,
                startDate = startDate!!,
                endDate = endDate!!,
                isCompleted = isCompleted
            )
            upsertPreorderUseCase(model, id != null)
            onSuccess()
        }
    }

    fun delete(id: String, onDone: () -> Unit) {
        viewModelScope.launch {
            getPreorderDetailUseCase(id)?.let {
                deleteUseCase(it)
                onDone()
            }
        }
    }
}