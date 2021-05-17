package net.sinasoheili.best_sellers.util

sealed class DataState<out R> {
    data class Success<T>(val data: T) : DataState<T>()
    class Loading : DataState<Nothing>()
    data class ConnectionError(val exception: Exception ) : DataState<Nothing>()
    data class Error(val text: String ) : DataState<Nothing>()
}