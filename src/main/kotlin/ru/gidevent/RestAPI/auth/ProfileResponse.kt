package ru.gidevent.RestAPI.auth


data class ProfileResponse(
        val id: Long = 0,
        val login: String,
        val firstName: String,
        val lastName: String,
        val roles: Set<Role>
)