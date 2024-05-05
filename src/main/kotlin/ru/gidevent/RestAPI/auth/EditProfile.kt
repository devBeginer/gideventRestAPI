package ru.gidevent.RestAPI.auth


data class EditProfile(
    val id: Long,
    val photo: String,
    val firstName: String,
    val lastName: String,
    val about: String,
    val login: String,
    val password: String,
    val roles: Set<Role>,
    val vkUser: Boolean
)