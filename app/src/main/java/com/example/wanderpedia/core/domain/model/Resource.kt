package com.example.wanderpedia.core.domain.model

sealed class Resource<out D> {
    data class Success<out D>(val data: D) : Resource<D>()
    data class Error(val error: Throwable) : Resource<Nothing>()
    object Loading : Resource<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
            Loading -> "Loading"
        }
    }

    val exception: Throwable?
        get() = when (this) {
            is Success -> null
            is Error -> error
            Loading -> null
        }
}

inline fun <T, R> T.safeResource(block: T.() -> R): Resource<R> {
    return try {
        Resource.Success(block())
    } catch (e: Throwable) {
        e.printStackTrace()
        Resource.Error(e)
    }
}

