package com.example.pomaryapp.core.utils

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class StringText {
    data class DynamicString(val value: String) : StringText()
    class StringResource(@StringRes val resId: Int, vararg val args: Any) : StringText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
        }
    }
}