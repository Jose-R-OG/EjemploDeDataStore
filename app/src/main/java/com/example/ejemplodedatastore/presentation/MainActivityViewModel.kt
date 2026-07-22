package com.example.ejemplodedatastore.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ejemplodedatastore.data.AppStatsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class MainActivityState(
    val openCount: Int = 0,
    val formattedLastOpenedDate: String = "",
    val isLoading: Boolean = true
)

sealed class MainActivityEvents {
    object RecordAppOpen : MainActivityEvents()
}

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: AppStatsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(MainActivityState())
    val state: StateFlow<MainActivityState> = _state.asStateFlow()

    init {
        observeStats()
    }

    private fun observeStats() {
        viewModelScope.launch {
            repository.appStatsFlow
                .collect { protoStats ->
                    _state.update {
                        it.copy(
                            openCount = protoStats.openCount,
                            formattedLastOpenedDate = formatTimestamp(protoStats.lastOpenedTimestamp),
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun processIntent(intent: MainActivityEvents) {
        when (intent) {
            is MainActivityEvents.RecordAppOpen -> {
                viewModelScope.launch {
                    repository.recordAppOpen(System.currentTimeMillis())
                }
            }
        }
    }

    private fun formatTimestamp(timestamp: Long): String {
        if (timestamp == 0L) return "Esta es la primera vez"
        val sdf = SimpleDateFormat("dd/MM/yyyy 'a las' HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}