package com.example.pomaryapp.data.mapper

import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.remote.dto.UserDto
import com.example.pomaryapp.domain.model.UserModel
import com.google.firebase.auth.FirebaseUser

fun UserDto.toDomain(): UserModel{
    return UserModel(
        userId = userId,
        name = name,
        email = email,
        pin = pin,
        messageTemplate = messageTemplate,
        hasCompletedSetup = hasCompletedSetup
    )
}

fun UserModel.toDto(): UserDto {
    return UserDto(
        userId = userId,
        name = name,
        email = email,
        pin = pin,
        messageTemplate = messageTemplate,
        hasCompletedSetup = hasCompletedSetup
    )
}

fun FirebaseUser.toDomain(): UserModel {
    return UserModel(
        userId = uid,
        name = displayName ?: "Pemilik Usaha",
        email = email ?: "",
        pin = "",
        messageTemplate = Constants.DEFAULT_TEMPLATE,
        hasCompletedSetup = false
    )
}