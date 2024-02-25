package ru.gidevent.RestAPI.auth

data class JwtUserResponse(
        val type: String = "Bearer",
        val accessToken:String?,
        val refreshToken:String?
)