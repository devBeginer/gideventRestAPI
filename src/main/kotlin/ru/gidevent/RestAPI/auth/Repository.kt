package ru.gidevent.RestAPI.auth

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface Repository: CrudRepository<User, Long> {
    /*@Query("SELECT * FROM USERS WHERE login = :login", nativeQuery = true)
    //fun getUserByLogin(@Param("login") login: String): User?
    fun getUserByLogin(@Param("login") login: String): Optional<User>*/
    fun findByLogin(login: String): Optional<User>
}