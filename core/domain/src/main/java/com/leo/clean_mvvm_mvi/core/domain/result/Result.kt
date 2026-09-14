package com.leo.clean_mvvm_mvi.core.domain.result

sealed interface Result<out D, out E : DomainError> {
    data class Success<out D>(val data: D) : Result<D, Nothing>
    data class Error<out E : DomainError>(val error: E) : Result<Nothing, E>

    fun <R> map(transform: (D) -> R): Result<R, E> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(error)
    }
}

inline fun <D, E : DomainError> Result<D, E>.onSuccess(action: (D) -> Unit): Result<D, E> {
    if (this is Result.Success) action(data)
    return this
}

inline fun <D, E : DomainError> Result<D, E>.onError(action: (E) -> Unit): Result<D, E> {
    if (this is Result.Error) action(error)
    return this
}

inline fun <D, E : DomainError, R> Result<D, E>.fold(
    onSuccess: (D) -> R,
    onError: (E) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(error)
}
