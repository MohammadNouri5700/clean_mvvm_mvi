package com.leo.clean_mvvm_mvi.core.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val driverDispatcher: DriverDispatchers)

enum class DriverDispatchers {
    IO,
    Default
}

