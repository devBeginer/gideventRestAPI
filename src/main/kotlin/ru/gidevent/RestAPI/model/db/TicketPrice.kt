package ru.gidevent.RestAPI.model.db

import jakarta.persistence.*
import ru.gidevent.RestAPI.model.db.Advertisement

@Entity
@Table(name="TICKET_PRICE")
data class TicketPrice(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "price_id")
        val priceId: Long = 0,
        @ManyToOne(targetEntity = Advertisement::class,  fetch= FetchType.LAZY/*EAGER*/, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "advertisement_id",name = "advertisement_id"/*, insertable = false, updatable = false*/)
        //@OnDelete(action = OnDeleteAction.CASCADE)
        val advertisement: Advertisement,
        @ManyToOne(targetEntity = CustomerCategory::class,  fetch= FetchType.LAZY/*EAGER*/, cascade = [CascadeType.ALL])
        @JoinColumn(referencedColumnName = "customer_category_id",name = "customer_category_id"/*, insertable = false, updatable = false*/)
        //@OnDelete(action = OnDeleteAction.CASCADE)
        val customerCategory: CustomerCategory,
        val price: Int
)
