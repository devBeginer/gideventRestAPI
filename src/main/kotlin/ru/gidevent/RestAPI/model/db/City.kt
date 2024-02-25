package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*


@Entity
@Table(name="CITY")
data class City(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "city_id")
        val cityId: Long,
        val name: String
)
