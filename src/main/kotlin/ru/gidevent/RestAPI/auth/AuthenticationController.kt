package ru.gidevent.RestAPI.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.gidevent.RestAPI.model.response.ResponseMessage
import java.util.*

@RestController
@RequestMapping("api")
class AuthenticationController {
    @Autowired
    lateinit var authService: AuthenticationService
    @Autowired
    lateinit var vkAuthService: VkAuthService

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

    @PostMapping("admin")
    fun registerAdmin(@RequestBody user: RegisterUserDto): ResponseEntity<*> {
        val profile = authService.getUserRecord()
        return if(profile.roles.contains(Role.ADMIN)){
            val registeredUser: User = authService.signup(user)

            ResponseEntity.ok(registeredUser)
        }else {
            ResponseEntity(ResponseMessage("Access denied"), HttpStatus.BAD_REQUEST)
        }
    }

    @PostMapping("auth/vkAuth")
    fun register(@RequestParam("accessToken") accessToken: String, @RequestParam("uuid") uuid: String): ResponseEntity<*> {
        val userResponse = vkAuthService.getVkUser(accessToken, uuid)?.response?.first()
        val user =  if(userResponse == null){
            null
        }else if(authService.isExistByVkId(userResponse.id)){
            userResponse.let {
                authService.loginFromVk(it.id)
            }
        }else{
            userResponse.let {
                val registeredUser: User = authService.signup(
                        RegisterUserDto(
                                UUID.randomUUID().toString(),
                                UUID.randomUUID().toString(),
                                it.first_name,
                                it.last_name,
                                it.photo_100,
                                "USER",
                                it.verified == 1,
                                it.id
                        )
                )
                authService.loginFromVk(registeredUser.vkId)
            }
        }
        return if(user!=null){
            ResponseEntity.ok(user)
        }else{
            ResponseEntity(ResponseMessage("Authentication error"), HttpStatus.BAD_REQUEST)
        }

    }
}