package ru.gidevent.RestAPI.auth

data class JwtUserRequest(
        val login: String,
        val password: String
)