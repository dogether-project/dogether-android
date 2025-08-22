package site.dogether.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.IOException

class DataStoreManager(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    private val safeData: Flow<Preferences> = dataStore.data.catch { e ->
        if (e is IOException) emit(emptyPreferences()) else throw e
    }

    suspend fun storeString(key: String, data: String): Result<Unit> = runCatching {
        dataStore.edit { it[stringPreferencesKey(key)] = data }
    }

    suspend fun loadString(key: String): Result<String> = runCatching {
        val prefs = safeData.first()
        prefs[stringPreferencesKey(key)].orEmpty()
    }

    suspend fun <T> storeDataObjectAsString(
        key: String,
        data: T,
        serializer: KSerializer<T>,
    ): Result<Unit> = runCatching {
        dataStore.edit { prefs ->
            prefs[stringPreferencesKey(key)] = json.encodeToString(serializer, data)
        }
    }

    suspend fun <T> loadObject(
        key: String,
        serializer: KSerializer<T>,
    ): Result<T?> = runCatching {
        val prefs = safeData.first()
        val raw = prefs[stringPreferencesKey(key)] ?: return@runCatching null
        json.decodeFromString(serializer, raw)
    }

    suspend fun deleteString(key: String): Result<Unit> = runCatching {
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }
}
