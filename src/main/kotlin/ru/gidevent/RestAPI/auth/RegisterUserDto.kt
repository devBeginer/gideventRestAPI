package ru.gidevent.RestAPI.auth

data class RegisterUserDto(
        val login: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val roles: String
)