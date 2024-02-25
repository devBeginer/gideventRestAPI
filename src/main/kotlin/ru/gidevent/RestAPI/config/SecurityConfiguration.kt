package ru.gidevent.RestAPI.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import ru.gidevent.RestAPI.auth.JwtFilter

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled=true)
class SecurityConfiguration(
        private val authenticationProvider: AuthenticationProvider,
        private val jwtAuthenticationFilter: JwtFilter
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {

        http.csrf { csrf -> csrf.disable() }
                /*http.csrf(AbstractHttpConfigurer::disable)
                        .disable()*/
                .authorizeHttpRequests { authorizeHttpRequests -> authorizeHttpRequests
                        //.requestMatchers("/auth/**")
                        .requestMatchers(AntPathRequestMatcher("/api/auth/**"))
                        //.requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                }

                //.and()
                .sessionManagement { sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
                /*.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()*/
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }


}