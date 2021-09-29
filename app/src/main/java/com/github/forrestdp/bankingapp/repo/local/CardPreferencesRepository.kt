package com.github.forrestdp.bankingapp.repo.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import com.github.forrestdp.bankingapp.di.IODispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CardPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) {
    private object PreferencesKeys {
        val LAST_OPEN_CARD_POSITION = intPreferencesKey("LAST_OPEN_CARD_POSITION")
    }

    val lastPosition = dataStore.data.map { it[PreferencesKeys.LAST_OPEN_CARD_POSITION] }

    suspend fun setLastPosition(position: Int) {
        withContext(ioDispatcher) {
            dataStore.edit {
                it[PreferencesKeys.LAST_OPEN_CARD_POSITION] = position
            }
        }
    }
}