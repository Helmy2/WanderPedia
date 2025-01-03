package com.example.wanderpedia.core.domain.model.usecase

sealed class DataResult<out D> {
    data class Success<out D>(val data: D) : DataResult<D>()
    data class Error(val error: Throwable) : DataResult<Nothing>()

    fun isSuccess() = this is Success
    fun isFailed() = this is Error

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[error=$error]"
        }
    }

    val exception: Throwable?
        get() = when (this) {
        is Success -> null
        is Error -> error
    }
}

inline fun <T, R> T.safeDataResult(block: T.() -> R): DataResult<R> {
    return try {
        DataResult.Success(block())
    } catch (e: Throwable) {
        e.printStackTrace()
        DataResult.Error(e)
    }
}

