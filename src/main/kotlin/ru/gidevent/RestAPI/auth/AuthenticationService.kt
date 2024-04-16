package ru.gidevent.RestAPI.auth

import jakarta.security.auth.message.AuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import io.jsonwebtoken.Claims

@Service
class AuthenticationService(
        /*private val repository: Repository,
        private val jwtProvider: JwtService*/
) {
    @Autowired
    lateinit var repository: Repository
    @Autowired
    lateinit var jwtProvider: JwtService

    private val refreshStorage: HashMap<String, String> = hashMapOf()



    fun login(authRequest: JwtUserRequest): JwtUserResponse {
        val user: User = repository.findByLogin(authRequest.login)
                .orElseThrow { AuthException("Пользователь не найден") }
        return if (user.password == authRequest.password) {
            val accessToken: String = jwtProvider.generateAccessToken(user)
            val refreshToken: String = jwtProvider.generateRefreshToken(user)
            refreshStorage[user.login] = refreshToken
            JwtUserResponse(accessToken = accessToken, refreshToken = refreshToken)
        } else {
            throw AuthException("Неправильный пароль")
        }
    }

    fun getAccessToken(refreshToken: String): JwtUserResponse {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            val claims: Claims = jwtProvider.getRefreshClaims(refreshToken)
            val login = claims.subject
            val saveRefreshToken: String? = refreshStorage[login]
            if (saveRefreshToken != null && saveRefreshToken == refreshToken) {
                val user: User = repository.findByLogin(login)
                        .orElseThrow { AuthException("Пользователь не найден") }
                val accessToken: String = jwtProvider.generateAccessToken(user)
                val refreshToken: String = jwtProvider.generateRefreshToken(user)
                refreshStorage[user.login] = refreshToken
                return JwtUserResponse(accessToken = accessToken, refreshToken = refreshToken)
            }
        }
        return JwtUserResponse(accessToken = null, refreshToken = null)
    }

    fun refresh(refreshToken: String): JwtUserResponse {
        if (jwtProvider.validateRefreshToken(refreshToken)) {
            val claims: Claims = jwtProvider.getRefreshClaims(refreshToken)
            val login = claims.subject
            val saveRefreshToken: String? = refreshStorage[login]
            if (saveRefreshToken != null && saveRefreshToken == refreshToken) {
                val user: User = repository.findByLogin(login)
                        .orElseThrow { AuthException("Пользователь не найден") }
                val accessToken: String = jwtProvider.generateAccessToken(user)
                val newRefreshToken: String = jwtProvider.generateRefreshToken(user)
                refreshStorage[user.login] = newRefreshToken
                return JwtUserResponse(accessToken = accessToken, refreshToken = newRefreshToken)
            }
        }
        throw AuthException("Невалидный JWT токен")
    }

    fun getAuthInfo(): JwtAuthentication {
        val tmp = SecurityContextHolder.getContext().authentication as JwtAuthentication
        tmp.name
        return SecurityContextHolder.getContext().authentication as JwtAuthentication
    }

    fun signup(registerUserDto: RegisterUserDto): User {
        val role = if (registerUserDto.roles == "ADMIN" ) {
            setOf(Role.ADMIN, Role.USER)
        } else if(registerUserDto.roles == "SELLER" ) {
            setOf(Role.SELLER, Role.USER)
        }else {
            setOf(Role.USER)
        }
        val user = User(login = registerUserDto.login, password = registerUserDto.password, firstName = registerUserDto.firstName, lastName = registerUserDto.lastName, photo = registerUserDto.photo, roles = role)
        return repository.save(user)
    }

    fun updateUser(id: Long, registerUserDto: RegisterUserDto): User? {
        val role = if (registerUserDto.roles == "ADMIN" ) {
            setOf(Role.ADMIN, Role.USER)
        } else if(registerUserDto.roles == "SELLER" ) {
            setOf(Role.SELLER, Role.USER)
        }else {
            setOf(Role.USER)
        }
        val user = User(login = registerUserDto.login, password = registerUserDto.password, firstName = registerUserDto.firstName, lastName = registerUserDto.lastName, photo = registerUserDto.photo, roles = role)
        return if(repository.existsById(id)){
            repository.save(user)
        }else{
            null
        }
    }


    fun getUser(): ProfileResponse {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthentication

        if (authentication.isAuthenticated) {
            val user = authentication.username?.let { repository.findByLogin(it) }?.get()

            if (user != null) {
                return ProfileResponse(user.id, user.login, user.firstName, user.lastName, user.roles)
            }
        }
        throw AuthException("Невалидный JWT токен")
    }


    fun getUserDetails(): User {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthentication

        if (authentication.isAuthenticated) {
            val user = authentication.username?.let { repository.findByLogin(it) }?.get()

            if (user != null) {
                return user
            }
        }
        throw AuthException("Невалидный JWT токен")
    }


    fun getUserRecord(): User {
        val authentication = SecurityContextHolder.getContext().authentication as JwtAuthentication

        if (authentication.isAuthenticated) {
            val user = authentication.username?.let { repository.findByLogin(it) }

            if (user != null) {
                return user.get()
            }
        }
        throw AuthException("Невалидный JWT токен")
    }
}