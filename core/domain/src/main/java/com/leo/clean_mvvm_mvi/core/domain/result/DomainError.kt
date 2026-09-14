package com.leo.clean_mvvm_mvi.core.domain.result

sealed interface DomainError {

    sealed interface NetworkError : DomainError {
        data object NoInternet : NetworkError
        data object Timeout : NetworkError
        data object ServerError : NetworkError
        data object Unauthorized : NetworkError
        data object SerializationError : NetworkError
        data class HttpError(val code: Int, val message: String) : NetworkError
        data class Unknown(val cause: Throwable? = null) : NetworkError
    }

    sealed interface AuthError : DomainError {
        data object InvalidCredentials : AuthError
        data object SessionExpired : AuthError
        data object UserNotFound : AuthError
        data object EmailAlreadyInUse : AuthError
        data object WeakPassword : AuthError
    }

    sealed interface LocationError : DomainError {
        data object PermissionDenied : LocationError
        data object LocationDisabled : LocationError
        data object WebSocketDisconnected : LocationError
        data class SignalLost(val reason: String) : LocationError
    }

    sealed interface ValidationError : DomainError {
        data object InvalidEmail : ValidationError
        data object PasswordTooShort : ValidationError
        data object EmptyField : ValidationError
        data class Custom(val reason: String) : ValidationError
    }

    sealed interface DatabaseError : DomainError {
        data object NotFound : DatabaseError
        data object DiskFull : DatabaseError
        data class Unknown(val cause: Throwable? = null) : DatabaseError
    }

    data class UnknownError(val cause: Throwable? = null) : DomainError
}
