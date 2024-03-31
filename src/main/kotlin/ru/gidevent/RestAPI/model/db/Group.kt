package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*

@Entity
@Table(name="GROUP")
data class Group(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "group_id")
        val id: Long=0,
        @ManyToOne(targetEntity = EventTime::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "customer_category_id")
        val customerCategory: CustomerCategory,
        val count: Int,
        @ManyToOne(targetEntity = Advertisement::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "booking_id")
        val booking: Booking
)
