package ru.gidevent.RestAPI.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api")
class AuthenticationController {
    @Autowired
    lateinit var authService: AuthenticationService

    @PreAuthorize("hasAuthority(USER)")
    @GetMapping("hello/user")
    fun helloUser(): ResponseEntity<String> {
        val authInfo: JwtAuthentication = authService.getAuthInfo()
        return ResponseEntity.ok(("Hello user " + authInfo.firstName) + "!")
    }

    @PreAuthorize("hasAuthority(T(ru.gidevent.RestAPI.auth.Role).ADMIN)")
    @GetMapping("hello/admin")
    fun helloAdmin(): ResponseEntity<String> {
        val authInfo: JwtAuthentication = authService.getAuthInfo()
        return ResponseEntity.ok(("Hello admin " + authInfo.firstName) + "!")
    }

    @PostMapping("auth/login")
    fun login(@RequestBody authRequest: JwtUserRequest): ResponseEntity<JwtUserResponse> {
        val token: JwtUserResponse = authService.login(authRequest)
        return ResponseEntity.ok<JwtUserResponse>(token)
    }

    @PostMapping("auth/token")
    fun getNewAccessToken(@RequestBody request: RefreshJwtRequest): ResponseEntity<JwtUserResponse> {
        val token: JwtUserResponse = authService.getAccessToken(request.refreshToken)
        return ResponseEntity.ok<JwtUserResponse>(token)
    }

    @PostMapping("auth/refresh")
    fun getNewRefreshToken(@RequestBody request: RefreshJwtRequest): ResponseEntity<JwtUserResponse> {
        val token: JwtUserResponse = authService.refresh(request.refreshToken)
        return ResponseEntity.ok<JwtUserResponse>(token)
    }

    @PostMapping("auth/signup")
    fun register(@RequestBody user: RegisterUserDto): ResponseEntity<User> {
        val registeredUser: User = authService.signup(user)
        return ResponseEntity.ok(registeredUser)
    }
}