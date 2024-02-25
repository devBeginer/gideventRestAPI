package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.Role

@Entity
@Table(name="SELLER")
data class Seller(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "seller_id")
        val sellerId: Long,
        val login: String,
        private val password: String,
        val firstName: String,
        val lastName: String,
        val photo: String,
        val about: String
)

//TODO сделать наследование от User (ссылку на User)
