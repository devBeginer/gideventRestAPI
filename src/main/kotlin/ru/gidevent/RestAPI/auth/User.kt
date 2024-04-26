package ru.gidevent.RestAPI.auth

import jakarta.persistence.*
import org.springframework.security.core.userdetails.UserDetails

@Entity
@Table(name="USERS")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "user_id")
        val id: Long = 0,
        @Column(unique=true)
        val login: String,
        private val password: String,
        val firstName: String,
        val lastName: String,
        val photo: String,
        val roles: Set<Role>,
        val vkId: String = "",
        val isVerified: Boolean = false
): UserDetails {
    override fun getAuthorities(): Set<Role> {
        return roles
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return login
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}