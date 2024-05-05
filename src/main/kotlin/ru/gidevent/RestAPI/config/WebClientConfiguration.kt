package ru.gidevent.RestAPI.config

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.Connection
import reactor.netty.http.client.HttpClient
import reactor.netty.tcp.TcpClient
import java.util.concurrent.TimeUnit


@Configuration
class WebClientConfiguration {
    @Bean
    fun webClientWithTimeout(): WebClient {
        val tcpClient = TcpClient
                .create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, TIMEOUT)
                .doOnConnected { connection: Connection ->
                    connection.addHandlerLast(ReadTimeoutHandler(TIMEOUT.toLong(), TimeUnit.MILLISECONDS))
                    connection.addHandlerLast(WriteTimeoutHandler(TIMEOUT.toLong(), TimeUnit.MILLISECONDS))
                }
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .clientConnector(ReactorClientHttpConnector(HttpClient.from(tcpClient)))
                .build()
    }

    /*@Bean
    fun webClient(): WebClient {
        return WebClient.builder()
                .baseUrl(BASE_URL)
                .defaultCookie("cookie-name", "cookie-value")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
    }*/

    companion object {
        private const val BASE_URL = "https://api.vk.com/method"
        const val TIMEOUT = 1000
    }
}