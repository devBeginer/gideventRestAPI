package ru.gidevent.RestAPI.auth

import org.springframework.security.core.Authentication

class JwtAuthentication : Authentication {
    private var authenticated = false
    var username: String? = null
    var firstName: String? = null
    var roles: Set<Role>? = null

    override fun getName(): String? {
        return firstName
    }

    override fun getAuthorities(): Set<Role>? {
        return roles
    }

    override fun getCredentials(): Any? {
        return null
    }

    override fun getDetails(): Any? {
        return null
    }

    override fun getPrincipal(): Any? {
        return null
    }

    override fun isAuthenticated(): Boolean {
        return authenticated
    }

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated;
    }

}