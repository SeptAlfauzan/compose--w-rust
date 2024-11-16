package org.kudos.rustoncompose.utils

sealed class UiState<out T>(){
    data class Success<T>(val data: T) : UiState<T>()
    data object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
}