package com.example.pomaryapp.data.repository

import android.content.Context
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.local.preferences.AuthPreferences
import com.example.pomaryapp.data.mapper.toDomain
import com.example.pomaryapp.data.mapper.toDto
import com.example.pomaryapp.data.remote.dto.UserDto
import com.example.pomaryapp.domain.model.UserModel
import com.example.pomaryapp.domain.repository.AuthRepository
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val credentialManager: CredentialManager,
    private val authPreferences: AuthPreferences,
    @ApplicationContext private val context: Context
): AuthRepository {
    override suspend fun signInWithGoogle(): Result<UserModel?> {
        return try{
            val googleIdTokenOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId("14410136004-cvbui5101gremc5jla58hlkq1rdjtlg3.apps.googleusercontent.com")
                .setAutoSelectEnabled(true)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdTokenOption)
                .build()

            val result = credentialManager.getCredential(context, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL){
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val firebaseUser = authResult.user ?: throw Exception("User not exist")

                val userDoc = firestore.collection(Constants.COLL_USERS).document(firebaseUser.uid).get().await()

                val userModel = if (userDoc.exists()) {
                    userDoc.toObject(UserDto::class.java)?.toDomain()
                } else {
                    val newUser = firebaseUser.toDomain()
                    firestore.collection(Constants.COLL_USERS).document(firebaseUser.uid).set(newUser.toDto()).await()
                    newUser
                }
                userModel?.let{
                    authPreferences.saveAuthData(it.userId, it.name, it.pin, it.messageTemplate)
                }
                Result.success(userModel)
            }else {
                Result.failure(Exception("Unsupported credential type"))
            }
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
        firestore.collection(Constants.COLL_USERS).document(uid)
            .update("pin", pin, "hasCompletedSetup", true).await()
        val session = getSessionData().first()
        authPreferences.saveAuthData(uid, session.name, pin, session.messageTemplate)
    }

    override suspend fun getPin(): String? = authPreferences.userPin.first()

    override suspend fun validatePin(inputPin: String): Boolean = getPin() == inputPin

    override suspend fun updateName(newName: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection(Constants.COLL_USERS).document(uid).update("name", newName).await()
        authPreferences.updateName(newName)
    }

    override suspend fun updateMessageTemplate(newTemplate: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection(Constants.COLL_USERS).document(uid).update("messageTemplate", newTemplate).await()
        authPreferences.updatePin(newTemplate)
    }

    override suspend fun logout() {
        auth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        authPreferences.clear()
    }
}