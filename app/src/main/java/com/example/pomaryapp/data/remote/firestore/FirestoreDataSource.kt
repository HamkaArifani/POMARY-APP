package com.example.pomaryapp.data.remote.firestore

import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.remote.dto.UserDto
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    private fun userCol() = firestore.collection(Constants.COLL_USERS)

    suspend fun getUser(uid: String): UserDto? {
        return try{
            userCol().document(uid).get().await().toObject(UserDto::class.java)
        } catch (e: Exception){
            Timber.e(e, "Gagal mengambil data user, jaringan bermasalah atau belum ada di cloud")
            null
        }
    }

    suspend fun saveUser(user: UserDto) {
        userCol().document(user.userId).set(user).await()
    }

    suspend fun updateUserFields(uid: String, updates: Map<String, Any>) {
        userCol().document(uid).update(updates).await()
    }
}