package ru.gidevent.RestAPI.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.gidevent.RestAPI.auth.accessTokenResponse.VkAccessToken
import ru.gidevent.RestAPI.auth.accessTokenResponse.VkAccessTokenResponse
import ru.gidevent.RestAPI.auth.userResponse.VkUser
import ru.gidevent.RestAPI.auth.userResponse.VkUserResponse


@Service
class VkAuthService(
        @Value("\${vk_service}") val service_token: String,
        @Value("\${vk_secret}") val vk_secret: String
) {
    @Autowired
    lateinit var webClient: WebClient

    fun getAccessTokenAsync(silentToken: String, uuid: String): VkAccessTokenResponse?/*Mono<VkAccessTokenResponse>*/ {

        val BASE_URL = "https://oauth.vk.com/access_token"

        //val url =     "https://api.vk.com/method/auth.exchangeSilentAuthToken?v=5.199&token=$silentToken&uuid=$uuid&access_token=$service_token"
        val url =     "/auth.exchangeSilentAuthToken?v=5.199&token=$silentToken&uuid=$uuid&access_token=$service_token"
        return webClient
                .get()
                //.uri("/?&code=$silentToken&client_secret=$vk_secret&redirect_uri=vk51914694://vk.com&client_id=51914694")
                .uri(url)
                .retrieve()
                .bodyToMono(VkAccessTokenResponse::class.java)
                .block()

    }


    fun getVkUser(silentToken: String, uuid: String): VkUserResponse?{
        val BASE_URL = "https://api.vk.com/method"
        val response = getAccessTokenAsync(silentToken, uuid)
        println(response.toString())
        val user = webClient
                .get()
                .uri("/users.get?user_ids=${response?.response?.user_id}&fields=photo_100,is_verified&v=5.131&access_token=${response?.response?.access_token}")
                .retrieve()
                .bodyToMono(VkUserResponse::class.java)
                .block()
        return user
        /*return getAccessTokenAsync(silentToken, uuid)
                .subscribe(
                        {
                            println(it.toString())
                            webClient
                                    .get()
                                    .uri("/users.get?user_ids=${it.user_id}&fields=photo_1,verified00&v=5.131&access_token=${it.access_token}")
                                    .retrieve()
                                    .bodyToMono(VkUserResponse::class.java)
                                            .block()
                        },
                        {
                            null
                        }
                )*/
    }
}