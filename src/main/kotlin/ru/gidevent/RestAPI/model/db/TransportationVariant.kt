package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*

@Entity
@Table(name="TRANSPORT")
data class TransportationVariant(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "transportation_id")
        val transportationId: Long = 0,
        val name: String
)
