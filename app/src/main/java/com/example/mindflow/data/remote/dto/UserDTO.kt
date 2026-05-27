package com.example.mindflow.data.remote.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class UserDTO(
    @SerializedName("id")
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("mail")
    val mail: String,
    @SerializedName("is_subscribed")
    val isSubscribed: Boolean
)
