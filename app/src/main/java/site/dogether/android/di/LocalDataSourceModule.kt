package site.dogether.android.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import site.dogether.common.IoDispatcher
import site.dogether.data.local.DataStoreManager

val localDataSourceModule = module {
    single<DataStore<Preferences>> {
        val context: Context = androidContext()
        val ioDispatcher: CoroutineDispatcher = get(named(IoDispatcher))
        PreferenceDataStoreFactory.create(
            scope = CoroutineScope(SupervisorJob() + ioDispatcher),
            produceFile = { context.preferencesDataStoreFile("app_prefs.preferences_pb") }
        )
    }
    single<DataStoreManager> {
        DataStoreManager(dataStore = get())
    }
}