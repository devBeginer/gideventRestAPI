package ru.gidevent.RestAPI.auth

import io.jsonwebtoken.Claims
import java.util.stream.Collectors

object JwtUtils {
    fun generate(claims: Claims): JwtAuthentication {
        val jwtInfoToken = JwtAuthentication()
        jwtInfoToken.roles = getRoles(claims)
        jwtInfoToken.firstName = claims["firstName", String::class.java]
        jwtInfoToken.username = claims.subject

        return jwtInfoToken
    }

    private fun getRoles(claims: Claims): Set<Role> {
        //val roles: List<String> = claims.get<List<*>>("roles", MutableList::class.java)
        //val roles: List<String> = claims.get("roles", List<String>::class)
        //val roles: List<String> = claims["roles", List<String>::class.java]
        /*val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        val rolesString: String = claims["roles", String::class.java]
        val list: List<String> = emptyList()
        val roles: List<String> = gson.fromJson(rolesString, list.javaClass);*/

        val list: ArrayList<String> = arrayListOf()
        val roles: List<String> = claims["roles", list.javaClass]
        return roles.stream()
                .map { value: String? -> Role.valueOf(value!!) }
                .collect(Collectors.toSet())
    }
}