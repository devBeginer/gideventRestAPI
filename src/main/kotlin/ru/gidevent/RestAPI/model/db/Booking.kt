package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.auth.User
import java.util.Calendar

@Entity
@Table(name="BOOKING")
data class Booking(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "booking_id")
        val id: Long=0,
        @ManyToOne(targetEntity = EventTime::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "time_id")
        val eventTime: EventTime,
        @ManyToOne(targetEntity = User::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "user_id")
        val user: User,
        @ManyToOne(targetEntity = Advertisement::class, fetch= FetchType.LAZY/*EAGER*/)
        @JoinColumn(referencedColumnName = "advertisement_id")
        val advertisement: Advertisement,
        val bookingTime: Calendar,
        val date: Calendar,
        val totalPrice: Int,
        val idApproved: Boolean
)
