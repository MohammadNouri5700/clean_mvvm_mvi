package com.leo.clean_mvvm_mvi.core.location.domain.model

data class LocationPoint(
    val latitude: Double,
    val longitude: Double,
    val accuracy: Float = 0f,
    val speed: Float = 0f,
    val timestampEpochMs: Long = System.currentTimeMillis()
)
