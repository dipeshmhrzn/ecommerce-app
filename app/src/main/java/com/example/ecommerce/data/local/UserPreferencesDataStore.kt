package com.example.ecommerce.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(private val context: Context) {

    companion object{
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")

        private val Is_First_Time_Login = booleanPreferencesKey("is_first_time_login")

        private val Is_Logged_In = booleanPreferencesKey("is_logged_in")
    }

    val isFirstTimeLogin : Flow<Boolean> = context.dataStore.data.map {preferences ->
        preferences[Is_First_Time_Login] ?: true
    }

    val isLoggedIn : Flow<Boolean> =context.dataStore.data.map {preferences ->
        preferences[Is_Logged_In] ?: false
    }

    suspend fun setFirstTimeLogin(isFirstTime: Boolean){
        context.dataStore.edit { preferences ->
            preferences[Is_First_Time_Login] = isFirstTime
        }
    }

    suspend fun setLoggedIn(isLoggedIn: Boolean){
        context.dataStore.edit { preferences ->
            preferences[Is_Logged_In] = isLoggedIn
        }
    }

}