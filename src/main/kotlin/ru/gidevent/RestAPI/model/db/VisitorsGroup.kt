package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*

@Entity
@Table(name = "VISITORS_GROUP")
data class VisitorsGroup(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "visitors_group_id")
        val id: Long = 0,
        @ManyToOne(targetEntity = CustomerCategory::class, fetch = FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "customer_category_id", name = "customer_category_id"/*, insertable = false, updatable = false*/)
        val customerCategory: CustomerCategory,
        val count: Int,
        @ManyToOne(targetEntity = Booking::class, fetch = FetchType.LAZY/*EAGER, cascade = [CascadeType.ALL]*/)
        @JoinColumn(referencedColumnName = "booking_id", name = "booking_id"/*, insertable = false, updatable = false*/)
        val booking: Booking
)
