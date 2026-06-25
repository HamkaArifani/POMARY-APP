package com.example.pomaryapp.data.remote.auth

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import com.example.pomaryapp.R
import com.example.pomaryapp.core.utils.Constants
import com.example.pomaryapp.data.local.preferences.AuthPreferences
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class GoogleAuthDataSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val authPreferences: AuthPreferences,
    private val credentialManager: CredentialManager,
    @ApplicationContext private val context: Context
){
    suspend fun getFirebaseUser(): Result<FirebaseUser> {
        return try {
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

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()

                val user = authResult.user
                if (user != null) {
                    Timber.i("Sign In berhasil untuk user: ${user.email}")
                    Result.success(user)
                } else {
                    val errorMsg = Constants.ERROR_LOGIN_MESSAGE
                    Timber.e(errorMsg)
                    Result.failure(Exception(errorMsg))
                }
            } else {
                val errorMsg = Constants.ERROR_CREDENTIAL_MESSAGE
                Timber.w(errorMsg)
                Result.failure(Exception(errorMsg))
            }
        }catch (e: Exception){
            Timber.e(e, "Terjadi kesalahan pada GoogleAuthDataSource")
            Result.failure(e)
        }
    }

    suspend fun signOut() {
        auth.signOut()
        credentialManager.clearCredentialState(ClearCredentialStateRequest())
        authPreferences.clear()
    }
}