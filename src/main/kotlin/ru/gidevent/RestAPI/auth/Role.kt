package ru.gidevent.RestAPI.auth

import org.springframework.security.core.GrantedAuthority

enum class Role: GrantedAuthority {
    ADMIN,
    SELLER,
    USER;

    override fun getAuthority(): String {
        return name
    }

}