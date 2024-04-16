package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.Role
import ru.gidevent.RestAPI.auth.User

@Entity
@Table(name="SELLER")
data class Seller(
        @Id
        //@GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "seller_id")
        val sellerId: Long,
        @OneToOne(targetEntity = User::class,  fetch= FetchType.LAZY, cascade = [CascadeType.ALL])
        //@JoinColumn(referencedColumnName = "user_id")
        @PrimaryKeyJoinColumn(name="seller_id", referencedColumnName="user_id")
        val user: User,
        val about: String
)

