package com.leo.clean_mvvm_mvi.core.network.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class SendOtpRequest(
    @field:SerializedName("phone_number")
    val phoneNumber: String,
)

data class OtpResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("data")
    val data: Any?,
    @field:SerializedName("error")
    val error: String?,
)

data class VerifyOtpRequest(
    @field:SerializedName("phone_number")
    val phoneNumber: String,
    @field:SerializedName("otp")
    val otp: String,
)

data class VerifyOtpResponse(
    @field:SerializedName("status")
    val status: String,
    @field:SerializedName("data")
    val data: VerifyOtpData?,
    @field:SerializedName("error")
    val error: String?,
)

data class VerifyOtpData(
    @field:SerializedName("access")
    val access: String,
    @field:SerializedName("refresh")
    val refresh: String,
    @field:SerializedName("driver")
    val driver: Driver,
)

data class LoginRequest(
    val username: String,
    val password: String,
)

data class OAuth(
    @field:SerializedName("access")
    val access: String? = null,
    @field:SerializedName("refresh")
    val refresh: String? = null,
    @field:SerializedName("session_id")
    val sessionId: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(access)
        parcel.writeString(refresh)
        parcel.writeString(sessionId)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<OAuth> {
        override fun createFromParcel(parcel: Parcel): OAuth = OAuth(parcel)
        override fun newArray(size: Int): Array<OAuth?> = arrayOfNulls(size)
    }
}

data class Driver(
    @field:SerializedName("driver_id")
    val driverId: String? = null,
    @field:SerializedName("first_name")
    val firstName: String? = null,
    @field:SerializedName("last_name")
    val lastName: String? = null,
    @field:SerializedName("phone_number")
    val phoneNumber: String? = null,
    @field:SerializedName("avatar")
    val avatar: String? = null,
    @field:SerializedName("username")
    val username: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(driverId)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
        parcel.writeString(avatar)
        parcel.writeString(username)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Driver> {
        override fun createFromParcel(parcel: Parcel): Driver = Driver(parcel)
        override fun newArray(size: Int): Array<Driver?> = arrayOfNulls(size)
    }
}

data class RefreshTokenRequest(
    @field:SerializedName("refresh")
    val refresh: String,
)

data class RefreshTokenResponse(
    @field:SerializedName("status")
    val status: String?,
    @field:SerializedName("data")
    val data: RefreshTokenData?,
    @field:SerializedName("error")
    val error: String?,
)

data class RefreshTokenData(
    @field:SerializedName("access")
    val access: String,
)

