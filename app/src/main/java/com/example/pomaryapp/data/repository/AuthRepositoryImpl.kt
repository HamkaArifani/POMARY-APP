package com.example.pomaryapp.data.repository

import android.content.Context
import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.local.preferences.AuthPreferences
import com.example.pomaryapp.data.mapper.toDomain
import com.example.pomaryapp.data.mapper.toDto
import com.example.pomaryapp.data.remote.auth.GoogleAuthDataSource
import com.example.pomaryapp.data.remote.firestore.FirestoreDataSource
import com.example.pomaryapp.domain.model.UserModel
import com.example.pomaryapp.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val googleAuthSource: GoogleAuthDataSource,
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val firestoreSource: FirestoreDataSource,
    private val authPreferences: AuthPreferences,
    @ApplicationContext private val context: Context
): AuthRepository {
    override suspend fun signInWithGoogle(): Result<UserModel?> {
        return try{
            Timber.d("Repository: Memulai alur Sign In Google")

            val loginResult = googleAuthSource.getFirebaseUser()
            val firebaseUser = loginResult.getOrThrow()

            val remoteUserDto = firestoreSource.getUser(firebaseUser.uid)

            val userModel = if (remoteUserDto != null){
                Timber.i("User lama terdeteksi, mengambil profil dari Firestore")
                remoteUserDto.toDomain()
            }else {
                Timber.i("User baru terdeteksi, membuat profil awal di Firestore")
                val newUserModel = firebaseUser.toDomain()
                firestoreSource.saveUser(newUserModel.toDto())
                newUserModel
            }
            userModel?.let {
                authPreferences.saveAuthData(
                    userId = it.userId,
                    name = it.name,
                    pin = it.pin,
                    messageTemplate = it.messageTemplate
                )
            }
            Timber.d("Proses Sign In Repository selesai dengan sukses")
            Result.success(userModel)
        } catch (e: Exception){
            Timber.e(e, "Sign In Google Error")
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun getSessionData(): Flow<UserModel> = authPreferences.userData.map{ (id, name, template) ->
        UserModel(
            id,
            name,
            auth.currentUser?.email ?: "",
            "",
            template,
            true
        )
    }

    override fun isSetupCompleted(): Flow<Boolean> = authPreferences.isSetupCompleted

    override suspend fun savePin(pin: String) {
        val uid = auth.currentUser?.uid ?: return
        firestoreSource.updateUserFields(uid, mapOf(
            "pin" to pin,
            "hasCompletedSetup" to true
        ))
        val session = getSessionData().first()
        authPreferences.saveAuthData(uid, session.name, pin, session.messageTemplate)
    }

    override suspend fun updatePin(newPin: String) {
        try {
            val uid = auth.currentUser?.uid ?: return
            firestore.collection(Constants.COLL_USERS)
                .document(uid)
                .update("pin", newPin)
                .await()
            authPreferences.updatePin(newPin)
            Timber.d("PIN berhasil diperbarui")
        }catch (e: Exception){
            Timber.e(e, "Gagal memperbarui PIN")
            throw e
        }
    }

    override suspend fun getPin(): String? = authPreferences.userPin.first()

    override suspend fun validatePin(inputPin: String): Boolean {
        val currentTime = System.currentTimeMillis()
        val lockTime = authPreferences.lockoutTime.first()
        val attempts = authPreferences.pinAttempts.first()

        if (currentTime - lockTime < 1800000) {
            Timber.w("Akses ditolak: Masih dalam masa blokir")
            return false
        }

        val savedPin = getPin()
        val isMatch = savedPin == inputPin

        if(isMatch){
            authPreferences.updatePinAttempts(0)
            authPreferences.setLockoutTime(0L)
            Timber.i("PIN Benar: Akses diberikan")
            return true
        } else {
            val newAttempts = attempts + 1
            authPreferences.updatePinAttempts(newAttempts)

            Timber.w("PIN Salah: Percobaan ke-$newAttempts")
            if (newAttempts >= 5) {
                authPreferences.setLockoutTime(currentTime)
                Timber.e("Akses Diblokir: 5 kali salah. Silakan tunggu 30 menit.")
            }
            return false
        }
    }

    override fun getLockoutRemainingTime(): Flow<Long> = authPreferences.lockoutTime.map { lockTime->
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lockTime
        val thirtyMinutes = 1800000L

        if (diff < thirtyMinutes){
            (thirtyMinutes - diff) / 60000
        } else{
            0L
        }
    }

    override suspend fun updateName(newName: String) {
        val uid = auth.currentUser?.uid ?: return
        firestoreSource.updateUserFields(uid, mapOf("name" to newName))
        authPreferences.updateName(newName)
    }

    override suspend fun updateMessageTemplate(newTemplate: String) {
        val uid = auth.currentUser?.uid ?: return
        firestoreSource.updateUserFields(uid, mapOf("messageTemplate" to newTemplate))
        authPreferences.updateTemplate(newTemplate)
    }

    override suspend fun logout() {
        try {
            googleAuthSource.signOut()
            authPreferences.clear()
            Timber.i("Logout berhasil")
        }catch (e: Exception){
            Timber.e(e, "Gagal menjalankan proses logout")
        }
    }
}