package com.leo.clean_mvvm_mvi.core.location.websocket

import com.leo.clean_mvvm_mvi.core.location.domain.model.LocationPoint
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationWebSocketClient @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private var webSocket: WebSocket? = null

    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = okHttpClient.newWebSocket(request, object : WebSocketListener() {})
    }

    fun sendLocation(location: LocationPoint) {
        val payload = """{"lat":${location.latitude},"lng":${location.longitude},"time":${location.timestampEpochMs}}"""
        webSocket?.send(payload)
    }

    fun disconnect() {
        webSocket?.close(1000, "User disconnected")
        webSocket = null
    }
}
