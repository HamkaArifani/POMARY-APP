package com.example.pomaryapp.data.local.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.pomaryapp.core.utils.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val USER_ID = stringPreferencesKey("user_id")
    private val USER_NAME = stringPreferencesKey("user_name")
    private val USER_PIN = stringPreferencesKey("user_pin")
    private val MESSAGE_TEMPLATE = stringPreferencesKey(Constants.KEY_MESSAGE_TEMPLATE)
    private val SETUP_COMPLETED = booleanPreferencesKey("setup_completed")

    val userData: Flow<Triple<String, String, String>> = dataStore.data.map { prefs->
        Triple(
            prefs[USER_ID] ?: "",
            prefs[USER_NAME] ?: "Pemilik Usaha",
            prefs[MESSAGE_TEMPLATE] ?: Constants.DEFAULT_TEMPLATE
        )
    }

    val userPin: Flow<String?> = dataStore.data.map { it[USER_PIN] }
    val isSetupCompleted: Flow<Boolean> = dataStore.data.map { it[SETUP_COMPLETED]?: false }

    suspend fun saveAuthData(userId: String, name: String, pin: String, messageTemplate: String) {
        dataStore.edit { prefs->
            prefs[USER_ID] = userId
            prefs[USER_NAME] = name
            prefs[USER_PIN] = pin
            prefs[MESSAGE_TEMPLATE] = messageTemplate
            prefs[SETUP_COMPLETED] = true
        }
    }

    suspend fun updateTemplate(newTemplate: String){
        dataStore.edit { it[MESSAGE_TEMPLATE] = newTemplate }
    }

    suspend fun updateName(newName: String){
        dataStore.edit { it[USER_NAME] = newName }
    }

    suspend fun updatePin(newPin: String){
        dataStore.edit { it[USER_PIN] = newPin }
    }

    suspend fun clear(){
        dataStore.edit { it.clear() }
    }
}