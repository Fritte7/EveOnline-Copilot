package com.fritte.eveonline.data.network

import com.fritte.eveonline.ui.states.UiState

suspend fun <T> safeApiCall(block: suspend () -> T): UiState<T> {
    return try {
        UiState.Success(block())
    } catch (e: Exception) {
        UiState.Error(e.message ?: "Unknow error")
    }
}