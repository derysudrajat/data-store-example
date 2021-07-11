package id.derysudrajat.datastore

import android.app.Activity
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow


object LocalShared {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme")
    private val THEME_KEY = stringPreferencesKey("theme")

    suspend fun changeTheme(context: Context, currentTheme: String) {
        context.dataStore.edit { theme -> theme[THEME_KEY] = currentTheme }
        (context as Activity).recreate()
    }

    suspend fun getCurrentTheme(context: Context) = flow {
        val data = context.dataStore.data.first()[THEME_KEY]
        emit(data ?: "")
    }
}

object Theme{
    const val DARK = "dark"
    const val LIGHT = "light"
    const val DIM = "dim"
}